package demo.acl.facade.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.*;
import demo.request.ProductRequest;
import demo.acl.facade.ProductFacade;
import io.vrap.rmf.base.client.ApiHttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

import static demo.constant.ProductConstant.PRODUCT_ID;
import static demo.constant.ProductConstant.PRODUCT_NOT_FOUND;


/**
 * Facade layer for performing operations on product in commercetools SDK V2 version 7.3.0
 */
@Component
@Slf4j
@AllArgsConstructor
public class ProductFacadeImpl implements ProductFacade {


	private final ProjectApiRoot projectApiRoot;


	/**
	 * Method used for fetching all the products using get method from commercetools sdk v2
	 *
	 * @return object of type {@link CompletableFuture<ApiHttpResponse<ProductPagedQueryResponse>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<ProductPagedQueryResponse>> fetchProductsFacade() {

		var products = projectApiRoot.products().get().execute();
		log.info("Called commercetools sdk v2 get method for retrieve all products");
		return products;
	}


	/**
	 * Method used to fetch a product by id using get method from commercetools sdk v2
	 *
	 * @param productId the given id of the product to be fetched
	 * @return object of type {@link CompletableFuture<ApiHttpResponse<Product>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Product>> fetchProductByIdFacade(String productId) {

		var product = projectApiRoot.products().withId(productId).get().execute();
		log.info("Called commercetools sdk v2 get method for retrieve a product by id");
		return product;
	}


	/**
	 * Method used to set a new key on a product variant using an existing id of a product using post method from commercetools sdk v2
	 *
	 * @param productId      the given id of a product
	 * @param productRequest the given request object
	 * @return object of type {@link CompletableFuture<ApiHttpResponse<Product>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Product>> setKeyOnProductVariantFacade(String productId, ProductRequest productRequest) {

		var product = projectApiRoot.products()
				.withId(productId)
				.post(ProductUpdateBuilder.of()
						.version(projectApiRoot.products().withId(productId).get().execute()
								.thenApplyAsync(result -> result.getBody().getVersion())
								.handleAsync((result, ex) -> {
									if (ex != null) {
										throw new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_ID + productId + PRODUCT_NOT_FOUND);
									} else {
										return result;
									}
								})
								.join())
						.actions(ProductSetProductVariantKeyActionBuilder.of()
								.variantId(productRequest.getData().getAttributes().getMasterData().getCurrent().getMasterVariant().getId())
								.key(productRequest.getData().getAttributes().getMasterData().getCurrent().getMasterVariant().getKey())
								.build())
						.build())
				.execute();

		log.info("Called commercetools sdk v2 post method for setting a key on a product variant");
		return product;
	}


	/**
	 * Method used to set a new sku on a product variant using an existing id of a product using post method from commercetools sdk v2
	 *
	 * @param productId      the given id of a product
	 * @param productRequest the given request object
	 * @return object of type {@link CompletableFuture<ApiHttpResponse<Product>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Product>> setSkuOnProductVariantFacade(String productId, ProductRequest productRequest) {

		var product = projectApiRoot.products()
				.withId(productId)
				.post(ProductUpdateBuilder.of()
						.version(projectApiRoot.products().withId(productId).get().execute()
								.thenApplyAsync(result -> result.getBody().getVersion())
								.handleAsync((result, ex) -> {
									if (ex != null) {
										throw new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_ID + productId + PRODUCT_NOT_FOUND);
									} else {
										return result;
									}
								})
								.join())
						.actions(ProductSetSkuActionBuilder.of()
								.variantId(productRequest.getData().getAttributes().getMasterData().getCurrent().getMasterVariant().getId())
								.sku(productRequest.getData().getAttributes().getMasterData().getCurrent().getMasterVariant().getSku())
								.build())
						.build())
				.execute();

		log.info("Called commercetools sdk v2 post method for setting a sku on a product variant");
		return product;
	}


	/**
	 * Method used to save a new sku and a new product variant using an existing id of a product using post method from commercetools sdk v2
	 *
	 * @param productId      the given id of a product
	 * @param productRequest the given request object
	 * @return object of type {@link CompletableFuture<ApiHttpResponse<Product>>} from commercetools sdk v2
	 */
	@Override
	public CompletableFuture<ApiHttpResponse<Product>> saveProductVariantAndSkuFacade(String productId, ProductRequest productRequest) {

		var product = projectApiRoot.products()
				.withId(productId)
				.post(ProductUpdateBuilder.of()
						.version(projectApiRoot.products().withId(productId).get().execute()
								.thenApplyAsync(result -> result.getBody().getVersion())
								.handleAsync((result, ex) -> {
									if (ex != null) {
										throw new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_ID + productId + PRODUCT_NOT_FOUND);
									} else {
										return result;
									}
								})
								.join())
						.actions(ProductAddVariantActionBuilder.of()
								.sku(productRequest.getData().getAttributes().getMasterData().getCurrent().getVariant().getSku())
								.build())
						.build())
				.execute();

		log.info("Called commercetools sdk v2 post method for saving a sku and a product variant");
		return product;
	}
}
