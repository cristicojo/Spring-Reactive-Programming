package demo.service.impl;

import demo.acl.adapter.ProductAdapter;
import demo.request.ProductRequest;
import demo.response.ProductResponse;
import demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductAdapter productAdapter;


	/**
	 * Method used to get all products using ACL - anti corruption layer
	 *
	 * @return {@link Flux< ProductResponse >} object found
	 */
	@Override
	public Flux<ProductResponse> fetchProducts() {
		return productAdapter.fetchProductsAdapter();
	}


	/**
	 * Method used to get a product with a given id using ACL - anti corruption layer
	 *
	 * @param productId the id of the product
	 * @return {@link Mono<ProductResponse>} object found
	 */
	@Override
	public Mono<ProductResponse> fetchProductById(String productId) {
		return productAdapter.fetchProductByIdAdapter(productId);
	}


	/**
	 * Method used to change an existing key on an existing product variant using ACL - anti corruption layer
	 *
	 * @param productId      the id of the product
	 * @param productRequest the given request object
	 * @return the {@link Mono<ProductResponse>} object containing the new key
	 */
	@Override
	public Mono<ProductResponse> setKeyOnProductVariant(String productId, ProductRequest productRequest) {
		return productAdapter.setKeyOnProductVariantAdapter(productId, productRequest);
	}


	/**
	 * Method used to change an existing sku on an existing product variant using ACL - anti corruption layer
	 *
	 * @param productId      the id of the product
	 * @param productRequest the given request object
	 * @return the {@link Mono<ProductResponse>} object containing the new sku
	 */
	@Override
	public Mono<ProductResponse> setSkuOnProductVariant(String productId, ProductRequest productRequest) {
		return productAdapter.setSkuOnProductVariantAdapter(productId, productRequest);
	}


	/**
	 * Method used to save a new product variant with a new sku using ACL - anti corruption layer
	 *
	 * @param productId      the id of the product
	 * @param productRequest the given request object
	 * @return the {@link Mono<ProductResponse>} object containing the new sku and the new product variant
	 */
	@Override
	public Mono<ProductResponse> saveProductVariantAndSku(String productId, ProductRequest productRequest) {
		return productAdapter.saveProductVariantAndSkuAdapter(productId, productRequest);
	}
}