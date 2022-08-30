package demo.service;

import demo.request.ProductRequest;
import demo.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ProductService {

	Mono<ResponseEntity<ProductResponse>> fetchProducts();

	Mono<ProductResponse> fetchProductById(String productId);

	Mono<ProductResponse> setKeyOnProductVariant(String productId, ProductRequest productRequest);

	Mono<ProductResponse> setSkuOnProductVariant(String productId, ProductRequest productRequest);

	Mono<ProductResponse> saveProductVariantAndSku(String productId, ProductRequest productRequest);


}
