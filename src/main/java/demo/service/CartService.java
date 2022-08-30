package demo.service;

import demo.response.CartResponse;
import demo.request.CartRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface CartService {

	Mono<CartResponse> fetchCartById(String cartId, String locale, String channel);

	Mono<ResponseEntity<CartResponse>> fetchCarts(String locale, String channel);

	Mono<CartResponse> saveCart(CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> saveCartWithLineItems(CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> saveLineItem(String cartId, CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> removeLineItem(String cartId, CartRequest cartRequest, String locale, String channel);

	Mono<CartResponse> setLineItemQuantity(String cartId, CartRequest cartRequest, String locale, String channel);
}
