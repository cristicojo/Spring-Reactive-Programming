package demo.acl.adapter.impl;

import demo.acl.transformer.ProductTransformer;
import demo.request.badrequest.ProductBadRequest;
import demo.request.ProductRequest;
import demo.acl.adapter.ProductAdapter;
import demo.acl.facade.ProductFacade;
import demo.response.ProductResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

import static demo.constant.ProductConstant.PRODUCT_ID;
import static demo.constant.ProductConstant.PRODUCT_NOT_FOUND;


/**
 * Adapter layer for using reactive programming and also convert into Mono and Flux the Facade layer
 * Also in this class we transform Product from commercetools sdk to our ProductResponse object
 */
@Component
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductAdapterImpl implements ProductAdapter {


	ProductTransformer productTransformer;
	ProductFacade productFacade;
	ProductBadRequest productBadRequest;


	@Override
	public Mono<ResponseEntity<ProductResponse>> fetchProductsAdapter() {

		return Mono.fromFuture(productFacade.fetchProductsFacade().thenApplyAsync(result -> result.getBody().getResults()))
				.flatMap(result -> {
					ProductResponse resp = productTransformer.transformToProductResponse(result);
					return Objects.isNull(resp.getData()) ? Mono.just(ResponseEntity.noContent().build())
							: Mono.just(ResponseEntity.ok().body(resp));
				});
	}


	@Override
	public Mono<ProductResponse> fetchProductByIdAdapter(String productId) {

		return Mono.fromFuture(productFacade.fetchProductByIdFacade(productId))
				.onErrorResume(result -> {
					log.error(PRODUCT_ID + productId + PRODUCT_NOT_FOUND);
					return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
							PRODUCT_ID + productId + PRODUCT_NOT_FOUND,
							result));
				})
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())));
	}


	@Override
	public Mono<ProductResponse> setKeyOnProductVariantAdapter(String productId, ProductRequest productRequest) {

		return Mono.fromFuture(productFacade.setKeyOnProductVariantFacade(productId, productRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, productBadRequest.reason(productId, productRequest))))
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())));
	}


	@Override
	public Mono<ProductResponse> setSkuOnProductVariantAdapter(String productId, ProductRequest productRequest) {

		return Mono.fromFuture(productFacade.setSkuOnProductVariantFacade(productId, productRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, productBadRequest.reason(productId, productRequest))))
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())));
	}


	@Override
	public Mono<ProductResponse> saveProductVariantAndSkuAdapter(String productId, ProductRequest productRequest) {

		return Mono.fromFuture(productFacade.saveProductVariantAndSkuFacade(productId, productRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"A duplicate value '" + productRequest.getData().getAttributes().getMasterData().getCurrent().getVariant().getSku()
								+ "' exists for field 'sku' on one product variant.")))
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())));
	}
}
