package demo.acl.adapter.impl;

import demo.acl.transformer.ProductTransformer;
import demo.request.ProductBadRequest;
import demo.request.ProductRequest;
import demo.acl.adapter.ProductAdapter;
import demo.acl.facade.ProductFacade;
import demo.response.ProductResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static demo.constant.ProductConstant.PRODUCT_ID;
import static demo.constant.ProductConstant.PRODUCT_NOT_FOUND;


/**
 * Adapter layer for using reactive programming and also convert into Mono and Flux the Facade layer
 * Also in this class we transform Product from commercetools sdk to our ProductResponse object
 */
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductAdapterImpl implements ProductAdapter {


	ProductTransformer productTransformer;
	ProductFacade productFacade;
	ProductBadRequest productBadRequest;


	@Override
	public Flux<ProductResponse> fetchProductsAdapter() {

		return Mono.fromFuture(productFacade.fetchProductsFacade().thenApplyAsync(result -> result.getBody().getResults()))
				.map(productTransformer::transformToProductResponse)
				.flatMapMany(Flux::just)
				.log();

	}


	@Override
	public Mono<ProductResponse> fetchProductByIdAdapter(String productId) {

		return Mono.fromFuture(productFacade.fetchProductByIdFacade(productId))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_ID + productId + PRODUCT_NOT_FOUND)))
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())))
				.log();
	}


	@Override
	public Mono<ProductResponse> setKeyOnProductVariantAdapter(String productId, ProductRequest productRequest) {

		return Mono.fromFuture(productFacade.setKeyOnProductVariantFacade(productId, productRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, productBadRequest.reason(productId, productRequest))))
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())))
				.log();

	}


	@Override
	public Mono<ProductResponse> setSkuOnProductVariantAdapter(String productId, ProductRequest productRequest) {

		return Mono.fromFuture(productFacade.setSkuOnProductVariantFacade(productId, productRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, productBadRequest.reason(productId, productRequest))))
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())))
				.log();

	}


	@Override
	public Mono<ProductResponse> saveProductVariantAndSkuAdapter(String productId, ProductRequest productRequest) {

		return Mono.fromFuture(productFacade.saveProductVariantAndSkuFacade(productId, productRequest))
				.onErrorResume(result -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"A duplicate value '" + productRequest.getData().getAttributes().getMasterData().getCurrent().getVariant().getSku()
								+ "' exists for field 'sku' on one product variant.")))
				.map(result -> productTransformer.transformToProductResponse(Collections.singletonList(result.getBody())))
				.log();
	}
}
