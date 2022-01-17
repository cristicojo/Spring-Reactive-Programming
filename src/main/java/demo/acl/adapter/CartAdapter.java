package demo.acl.adapter;

import demo.request.CartRequest;
import demo.response.CartResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartAdapter {

	Mono<CartResponse> fetchCartByIdAdapter(String cartId, String locale, String channel);

	Flux<CartResponse> fetchCartsAdapter(String locale, String channel);

	Mono<CartResponse> saveCartAdapter(CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> saveCartWithLineItemsAdapter(CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> saveLineItemAdapter(String cartId, CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> removeLineItemAdapter(String cartId, CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> setLineItemQuantityAdapter(String cartId, CartRequest cartRequest, String locale, String channel);


}
