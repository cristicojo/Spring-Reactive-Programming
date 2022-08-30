package demo.controller;

import demo.request.CartRequest;
import demo.response.CartResponse;
import demo.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/cristi_project_key", consumes = "application/vnd.api+json", produces = "application/vnd.api+json")
@AllArgsConstructor
public class CartController {

	private final CartService cartService;




	@GetMapping("/carts/{id}")
	public Mono<CartResponse> getCartById(@PathVariable String id, @RequestParam String locale, @RequestParam String channel) {
		return cartService.fetchCartById(id, locale, channel);
	}


	@GetMapping("/carts")
	public Mono<ResponseEntity<CartResponse>> getCarts(@RequestParam String locale, @RequestParam String channel) {
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
