package demo.acl.facade.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.MoneyBuilder;
import demo.constant.CartConstant;
import demo.request.CartRequest;
import demo.acl.facade.CartFacade;
import io.vrap.rmf.base.client.ApiHttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;


/**
 * Facade layer for performing operations on cart in commercetools SDK V2 version 7.3.0
 */
@Component
@Slf4j
@AllArgsConstructor
public class CartFacadeImpl implements CartFacade {


	private final ProjectApiRoot projectApiRoot;


	/**
	 * Method used to get a cart by id using get method from commercetools sdk v2
	 *
	 * @param cartId the given id of the cart
	 * @return object of type {@link CompletableFuture<ApiHttpResponse<Cart>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Cart>> fetchCartByIdFacade(String cartId) {

		var cart = projectApiRoot.carts().withId(cartId).get().execute();
		log.info("Called commercetools sdk v2 get method for retrieve a cart by id");
		return cart;
	}


	/**
	 * Method used for fetching all the carts using get method from commercetools sdk v2
	 *
	 * @return object of type {@link CompletableFuture<ApiHttpResponse<CartPagedQueryResponse>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<CartPagedQueryResponse>> fetchCartsFacade() {

		var carts = projectApiRoot.carts().get().execute();
		log.info("Called commercetools sdk v2 get method for retrieve all carts");
		return carts;
	}


	/**
	 * Method used to save a cart using post method from commercetools sdk v2
	 *
	 * @param cartDraft the {@link CartDraft} object from commercetools sdk v2
	 * @return saved cart of type {@link CompletableFuture<ApiHttpResponse<Cart>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Cart>> saveCartFacade(CartDraft cartDraft) {

		var cart = projectApiRoot.carts().post(cartDraft).execute();
		log.info("Called commercetools sdk v2 post method for creating a cart");
		return cart;
	}


	/**
	 * Method used to save a cart with line items using post method from commercetools sdk v2
	 *
	 * @param cartDraft the {@link CartDraft} object from commercetools sdk v2 containing line items
	 * @return saved cart of type {@link CompletableFuture<ApiHttpResponse<Cart>>} from commercetools sdk v2 containing new line items
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Cart>> saveCartWithLineItemsFacade(CartDraft cartDraft) {

		var cart = projectApiRoot.carts().post(cartDraft).execute();
		log.info("Called commercetools sdk v2 post method for creating a cart with line items");
		return cart;

	}


	/**
	 * Method used to save a new line item in an existing cart using post method from commercetools sdk v2
	 *
	 * @param cartId      the given id of the cart
	 * @param cartRequest the {@link CartRequest} object containing a new line item to be saved
	 * @return cart of type {@link CompletableFuture<ApiHttpResponse<Cart>>} from commercetools sdk v2 containing the saved line item
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Cart>> saveLineItemFacade(String cartId, CartRequest cartRequest) {

		var cart = projectApiRoot.carts()
				.withId(cartId)
				.post(CartUpdateBuilder.of()
						.version(projectApiRoot.carts().withId(cartId).get().execute()
								.thenApplyAsync(result -> result.getBody().getVersion())
								.handleAsync((result, ex) -> {
									if (ex != null) {
										throw new ResponseStatusException(HttpStatus.NOT_FOUND, CartConstant.CART_ID + cartId + CartConstant.CART_NOT_FOUND);
									} else {
										return result;
									}
								})
								.join())
						.actions(CartAddLineItemActionBuilder.of()
								.sku(cartRequest.getData().getAttributes().getLineItems().get(0).getSku())
								.quantity(cartRequest.getData().getAttributes().getLineItems().get(0).getQuantity())
								.externalPrice(MoneyBuilder.of()
										.currencyCode(cartRequest.getData().getAttributes().getLineItems().get(0).getExternalPrice().getCurrencyCode())
										.centAmount(cartRequest.getData().getAttributes().getLineItems().get(0).getExternalPrice().getCentAmount())
										.build())
								.build())
						.build())
				.execute();

		log.info("Called commercetools sdk v2 post method for saving a new line item in an existing cart");
		return cart;

	}


	/**
	 * Method used to remove the line item by id from the cart (if the quantity is present than it decrease the quantity,
	 * if the quantity present is the same with the existing one than it removes the line item) using post method from commercetools sdk v2
	 *
	 * @param cartId      the given id of the cart
	 * @param cartRequest the {@link CartRequest} object containing the id of the line item to be removed
	 * @return cart of type {@link CompletableFuture<ApiHttpResponse<Cart>>} from commercetools sdk v2 without the removed line item
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Cart>> removeLineItemFacade(String cartId, CartRequest cartRequest) {

		var cart = projectApiRoot.carts()
				.withId(cartId)
				.post(CartUpdateBuilder.of()
						.version(projectApiRoot.carts().withId(cartId).get().execute()
								.thenApplyAsync(result -> result.getBody().getVersion())
								.handleAsync((result, ex) -> {
									if (ex != null) {
										throw new ResponseStatusException(HttpStatus.NOT_FOUND, CartConstant.CART_ID + cartId + CartConstant.CART_NOT_FOUND);
									} else {
										return result;
									}
								})
								.join())
						.actions(CartRemoveLineItemActionBuilder.of()
								.lineItemId(cartRequest.getData().getAttributes().getLineItems().get(0).getId())
								.quantity(cartRequest.getData().getAttributes().getLineItems().get(0).getQuantity())
								.externalPrice(MoneyBuilder.of()
										.currencyCode(cartRequest.getData().getAttributes().getLineItems().get(0).getExternalPrice().getCurrencyCode())
										.centAmount(cartRequest.getData().getAttributes().getLineItems().get(0).getExternalPrice().getCentAmount())
										.build())
								.build())
						.build())
				.execute();

		log.info("Called commercetools sdk v2 post method for removing a line item in an existing cart");
		return cart;
	}


	/**
	 * Method used to set the quantity of the line item in an existing cart using post method from commercetools sdk v2
	 *
	 * @param cartId      the given id of the cart
	 * @param cartRequest the {@link CartRequest} object containing the id of the line item to set the quantity of
	 * @return cart of type {@link CompletableFuture<ApiHttpResponse<Cart>>} from commercetools sdk v2 with the quantity set of the line item
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Cart>> setLineItemQuantityFacade(String cartId, CartRequest cartRequest) {

		var cart = projectApiRoot.carts()
				.withId(cartId)
				.post(CartUpdateBuilder.of()
						.version(projectApiRoot.carts().withId(cartId).get().execute()
								.thenApplyAsync(result -> result.getBody().getVersion())
								.handleAsync((result, ex) -> {
									if (ex != null) {
										throw new ResponseStatusException(HttpStatus.NOT_FOUND, CartConstant.CART_ID + cartId + CartConstant.CART_NOT_FOUND);
									} else {
										return result;
									}
								})
								.join())
						.actions(CartChangeLineItemQuantityActionBuilder.of()
								.lineItemId(cartRequest.getData().getAttributes().getLineItems().get(0).getId())
								.quantity(cartRequest.getData().getAttributes().getLineItems().get(0).getQuantity())
								.externalPrice(MoneyBuilder.of()
										.currencyCode(cartRequest.getData().getAttributes().getLineItems().get(0).getExternalPrice().getCurrencyCode())
										.centAmount(cartRequest.getData().getAttributes().getLineItems().get(0).getExternalPrice().getCentAmount())
										.build())
								.build())
						.build())
				.execute();

		log.info("Called commercetools sdk v2 post method for setting the quantity of the line item in an existing cart");
		return cart;
	}
}
