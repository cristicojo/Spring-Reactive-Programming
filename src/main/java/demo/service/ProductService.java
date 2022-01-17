package demo.service;

import demo.request.ProductRequest;
import demo.response.ProductResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

	Flux<ProductResponse> fetchProducts();

	Mono<ProductResponse> fetchProductById(String productId);

	Mono<ProductResponse> setKeyOnProductVariant(String productId, ProductRequest productRequest);

	Mono<ProductResponse> setSkuOnProductVariant(String productId, ProductRequest productRequest);

	Mono<ProductResponse> saveProductVariantAndSku(String productId, ProductRequest productRequest);


}
