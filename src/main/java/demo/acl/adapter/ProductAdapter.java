package demo.acl.adapter;

import demo.request.ProductRequest;
import demo.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductAdapter {

	Mono<ResponseEntity<ProductResponse>> fetchProductsAdapter();

	Mono<ProductResponse> fetchProductByIdAdapter(String productId);

	Mono<ProductResponse> setKeyOnProductVariantAdapter(String productId, ProductRequest productRequest);

	Mono<ProductResponse> setSkuOnProductVariantAdapter(String productId, ProductRequest productRequest);

	Mono<ProductResponse> saveProductVariantAndSkuAdapter(String productId, ProductRequest productRequest);
}
