package demo.controller;

import demo.request.CartRequest;
import demo.response.CartResponse;
import demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/lululemon", consumes = "application/vnd.api+json", produces = "application/vnd.api+json")
public class CartController {

	@Autowired
	private CartService cartService;




	@GetMapping("/carts/{id}")
	public Mono<CartResponse> getCartById(@PathVariable String id, @RequestParam String locale, @RequestParam String channel) {
		return cartService.fetchCartById(id, locale, channel);
	}


	@GetMapping("/carts")
	public Flux<CartResponse> getCarts(@RequestParam String locale, @RequestParam String channel) {
		return cartService.fetchCarts(locale, channel);
	}


	@PostMapping("/carts")
	public Mono<CartResponse> createCart(@Valid @RequestBody CartRequest cartRequest, @RequestParam String locale,
										 @RequestParam String channel) {

		return cartService.saveCart(cartRequest, locale, channel);
	}


	@PostMapping("/carts/lineItem")
	public Mono<CartResponse> createCartWithLineItems(@Valid @RequestBody CartRequest cartRequest, @RequestParam String locale,
													  @RequestParam String channel) {

		return cartService.saveCartWithLineItems(cartRequest, locale, channel);
	}


	@PostMapping("/carts/{id}")
	public Mono<CartResponse> addLineItemToCart(@PathVariable String id, @Valid @RequestBody CartRequest cartRequest,
												@RequestParam String locale, @RequestParam String channel) {

		return cartService.saveLineItem(id, cartRequest, locale, channel);
	}


	@PostMapping("/carts/lineItem/{id}")
	public Mono<CartResponse> removeLineItemFromCart(@PathVariable String id, @Valid @RequestBody CartRequest cartRequest,
													 @RequestParam String locale, @RequestParam String channel) {

		return cartService.removeLineItem(id, cartRequest, locale, channel);
	}


	@PostMapping("/carts/lineItemQuantity/{id}")
	public Mono<CartResponse> changeLineItemQuantity(@PathVariable String id, @Valid @RequestBody CartRequest cartRequest,
													 @RequestParam String locale, @RequestParam String channel) {

		return cartService.setLineItemQuantity(id, cartRequest, locale, channel);
	}
}
