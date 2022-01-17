package demo.acl.facade;

import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductPagedQueryResponse;
import demo.request.ProductRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.concurrent.CompletableFuture;

public interface ProductFacade {

	CompletableFuture<ApiHttpResponse<ProductPagedQueryResponse>> fetchProductsFacade();

	CompletableFuture<ApiHttpResponse<Product>> fetchProductByIdFacade(String productId);

	CompletableFuture<ApiHttpResponse<Product>> setKeyOnProductVariantFacade(String productId, ProductRequest productRequest);

	CompletableFuture<ApiHttpResponse<Product>> setSkuOnProductVariantFacade(String productId, ProductRequest productRequest);

	CompletableFuture<ApiHttpResponse<Product>> saveProductVariantAndSkuFacade(String productId, ProductRequest productRequest);
}
