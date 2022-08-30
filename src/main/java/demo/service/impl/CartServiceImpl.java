package demo.service.impl;

import demo.acl.adapter.CartAdapter;
import demo.request.CartRequest;
import demo.response.CartResponse;
import demo.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {


	private final CartAdapter cartAdapter;


	/**
	 * Method used to get a cart by id using ACL - anti corruption layer
	 *
	 * @param cartId  the id of the cart
	 * @param locale  the {@link String locale} request param
	 * @param channel the {@link String channel} request param
	 * @return the {@link Mono< CartResponse >} object which was found
	 */
	@Override
	public Mono<CartResponse> fetchCartById(String cartId, String locale, String channel) {
		return cartAdapter.fetchCartByIdAdapter(cartId, locale, channel);
	}


	/**
	 * Method used to get all carts using ACL - anti corruption layer
	 *
	 * @param locale  the {@link String locale} request param
	 * @param channel the {@link String channel} request param
	 * @return the {@link Flux<CartResponse>} object containing all the carts found
	 */
	@Override
	public Mono<ResponseEntity<CartResponse>> fetchCarts(String locale, String channel) {
		return cartAdapter.fetchCartsAdapter(locale, channel);
	}


	/**
	 * Method used to save a cart by using ACL - anti corruption layer
	 *
	 * @param cartRequest the {@link CartRequest} object
	 * @param locale      the {@link String locale} request param
	 * @param channel     the {@link String channel} request param
	 * @return saved {@link Mono<CartResponse>} object
	 */
	@Override
	public Mono<CartResponse> saveCart(CartRequest cartRequest, String locale, String channel) {
		return cartAdapter.saveCartAdapter(cartRequest, locale, channel);
	}


	/**
	 * Method used to save a cart with line items by using ACL - anti corruption layer
	 *
	 * @param cartRequest the {@link CartRequest} object
	 * @param locale      the {@link String locale} request param
	 * @param channel     the {@link String channel} request param
	 * @return saved {@link Mono<CartResponse>} object containing line items
	 */
	@Override
	public Mono<CartResponse> saveCartWithLineItems(CartRequest cartRequest, String locale, String channel) {
		return cartAdapter.saveCartWithLineItemsAdapter(cartRequest, locale, channel);
	}


	/**
	 * Method used to add a product variant in the given quantity to the cart by using ACL - anti corruption layer
	 *
	 * @param cartId      the id of the cart
	 * @param cartRequest the {@link CartRequest} object
	 * @param locale      the {@link String locale} request param
	 * @param channel     the {@link String channel} request param
	 * @return {@link Mono<CartResponse>} object containing a new line item
	 */
	@Override
	public Mono<CartResponse> saveLineItem(String cartId, CartRequest cartRequest, String locale, String channel) {
		return cartAdapter.saveLineItemAdapter(cartId, cartRequest, locale, channel);
	}


	/**
	 * Method used to remove the LineItem from the cart (if the quantity is present than it decrease the quantity)
	 * by using ACL - anti corruption layer
	 *
	 * @param cartId      the id of the cart
	 * @param cartRequest the {@link CartRequest} object
	 * @param locale      the {@link String locale} request param
	 * @param channel     the {@link String channel} request param
	 * @return {@link Mono<CartResponse>} object without the removed line item
	 */
	@Override
	public Mono<CartResponse> removeLineItem(String cartId, CartRequest cartRequest, String locale, String channel) {
		return cartAdapter.removeLineItemAdapter(cartId, cartRequest, locale, channel);
	}


	/**
	 * Method used to change the quantity number of the line item in the existing cart
	 *
	 * @param cartId      the id of the cart
	 * @param cartRequest the {@link CartRequest} object
	 * @param locale      the {@link String locale} request param
	 * @param channel     the {@link String channel} request param
	 * @return {@link Mono<CartResponse>} object containing the new quantity of the specific line item
	 */
	@Override
	public Mono<CartResponse> setLineItemQuantity(String cartId, CartRequest cartRequest, String locale, String channel) {
		return cartAdapter.setLineItemQuantityAdapter(cartId, cartRequest, locale, channel);
	}
}
