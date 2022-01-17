package demo.controller;

import demo.request.ProductRequest;
import demo.response.ProductResponse;
import demo.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/lululemon", consumes = "application/vnd.api+json", produces = "application/vnd.api+json")
public class ProductController {

	@Autowired
	private ProductServiceImpl productService;



	@GetMapping("/products")
	public Flux<ProductResponse> getProducts() {
		return productService.fetchProducts();
	}


	@GetMapping("/products/{id}")
	public Mono<ProductResponse> getProductById(@PathVariable String id) {
		return productService.fetchProductById(id);
	}


	@PostMapping("/products/key/{id}")
	public Mono<ProductResponse> setKeyToProductVariant(@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
		return productService.setKeyOnProductVariant(id, productRequest);
	}


	@PostMapping("/products/sku/{id}")
	public Mono<ProductResponse> setSkuToProductVariant(@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
		return productService.setSkuOnProductVariant(id, productRequest);
	}


	@PostMapping("/products/variant/{id}")
	public Mono<ProductResponse> addProductVariantAndSku(@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
		return productService.saveProductVariantAndSku(id, productRequest);
	}
}
