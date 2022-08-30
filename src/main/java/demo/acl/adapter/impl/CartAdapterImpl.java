package demo.acl.adapter.impl;

import demo.acl.adapter.CartAdapter;
import demo.acl.facade.CartFacade;
import demo.acl.transformer.CartTransformer;
import demo.constant.CartConstant;
import demo.request.badrequest.CartBadRequest;
import demo.request.CartRequest;
import demo.response.CartResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;


/**
 * Adapter layer for using reactive programming and also convert into Mono and Flux the Facade layer
 * Also in this class we transform Cart from commercetools sdk to our CartResponse object
 */
@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartAdapterImpl implements CartAdapter {


	CartTransformer cartTransformer;
	CartFacade cartFacade;
	CartBadRequest cartBadRequest;


	@Override
	public Mono<CartResponse> fetchCartByIdAdapter(String cartId, String locale, String channel) {

		return Mono.just(cartFacade.fetchCartByIdFacade(cartId))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result), locale, channel));
	}


	@Override
	public Mono<ResponseEntity<CartResponse>> fetchCartsAdapter(String locale, String channel) {

		return Mono.just(cartFacade.fetchCartsFacade())
				.flatMap(result -> {
					CartResponse resp = cartTransformer.transformToCartResponse(result, locale, channel);
					return Objects.isNull(resp.getData()) ? Mono.just(ResponseEntity.noContent().build())
							: Mono.just(ResponseEntity.ok().body(resp));
				});
	}


	@Override
	public Mono<CartResponse> saveCartAdapter(CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.saveCartFacade(cartTransformer.transformToCartDraft(cartRequest, locale)))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel));
	}


	@Override
	public Mono<CartResponse> saveCartWithLineItemsAdapter(CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.saveCartWithLineItemsFacade(cartTransformer.transformToCartDraftWithLineItems(cartRequest, locale)))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, cartBadRequest.reason(cartRequest))))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel));
	}


	@Override
	public Mono<CartResponse> saveLineItemAdapter(String cartId, CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.saveLineItemFacade(cartId, cartRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, cartBadRequest.reason(cartRequest))))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel));
	}


	@Override
	public Mono<CartResponse> removeLineItemAdapter(String cartId, CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.removeLineItemFacade(cartId, cartRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
						CartConstant.INVALID_LINE_ITEM_ID + cartRequest.getData().getAttributes().getLineItems().get(0).getId() + "'")))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel));
	}


	@Override
	public Mono<CartResponse> setLineItemQuantityAdapter(String cartId, CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.setLineItemQuantityFacade(cartId, cartRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
						CartConstant.INVALID_LINE_ITEM_ID + cartRequest.getData().getAttributes().getLineItems().get(0).getId() + "'")))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel));
	}
}
