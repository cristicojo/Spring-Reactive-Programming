package demo.acl.adapter.impl;

import demo.acl.adapter.CartAdapter;
import demo.acl.transformer.CartTransformer;
import demo.constant.CartConstant;
import demo.request.CartBadRequest;
import demo.request.CartRequest;
import demo.response.CartResponse;
import demo.acl.facade.CartFacade;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;


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

		return Mono.fromFuture(cartFacade.fetchCartByIdFacade(cartId))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, CartConstant.CART_ID + cartId + CartConstant.CART_NOT_FOUND)))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel))
				.log();
	}


	@Override
	public Flux<CartResponse> fetchCartsAdapter(String locale, String channel) {

		return Mono.fromFuture(cartFacade.fetchCartsFacade().thenApplyAsync(result -> result.getBody().getResults()))
				.map(result -> cartTransformer.transformToCartResponse(result, locale, channel))
				.flatMapMany(Flux::just)
				.log();

	}


	@Override
	public Mono<CartResponse> saveCartAdapter(CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.saveCartFacade(cartTransformer.transformToCartDraft(cartRequest, locale)))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel))
				.log();
	}


	@Override
	public Mono<CartResponse> saveCartWithLineItemsAdapter(CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.saveCartWithLineItemsFacade(cartTransformer.transformToCartDraftWithLineItems(cartRequest, locale)))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, cartBadRequest.reason(cartRequest))))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel))
				.log();
	}


	@Override
	public Mono<CartResponse> saveLineItemAdapter(String cartId, CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.saveLineItemFacade(cartId, cartRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, cartBadRequest.reason(cartRequest))))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel))
				.log();
	}


	@Override
	public Mono<CartResponse> removeLineItemAdapter(String cartId, CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.removeLineItemFacade(cartId, cartRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
						CartConstant.INVALID_LINE_ITEM_ID + cartRequest.getData().getAttributes().getLineItems().get(0).getId() + "'")))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel))
				.log();
	}


	@Override
	public Mono<CartResponse> setLineItemQuantityAdapter(String cartId, CartRequest cartRequest, String locale, String channel) {

		return Mono.fromFuture(cartFacade.setLineItemQuantityFacade(cartId, cartRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
						CartConstant.INVALID_LINE_ITEM_ID + cartRequest.getData().getAttributes().getLineItems().get(0).getId() + "'")))
				.map(result -> cartTransformer.transformToCartResponse(Collections.singletonList(result.getBody()), locale, channel))
				.log();

	}
}
