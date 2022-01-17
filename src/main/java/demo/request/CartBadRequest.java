package demo.request;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartBadRequest {

	@Autowired
	private ProjectApiRoot projectApiRoot;


	public String reason(CartRequest cartRequest) {

		List<Product> products = projectApiRoot.products().get().execute().join().getBody().getResults();

		List<String> masterVariantSkus = products.stream()
				.map(product -> product.getMasterData().getStaged().getMasterVariant().getSku())
				.collect(Collectors.toList());

		List<String> variantsSkus = products.stream().map(product -> product.getMasterData().getStaged().getVariants()
						.stream().map(ProductVariant::getSku)
						.collect(Collectors.toList()))
				.collect(Collectors.toList())
				.stream().flatMap(Collection::stream)
				.collect(Collectors.toList());

		masterVariantSkus.addAll(variantsSkus);

		List<String> skusRequest = cartRequest.getData().getAttributes().getLineItems().stream()
				.map(LineItemRequest::getSku)
				.collect(Collectors.toList());

		return skusRequest.stream().map(skuRequest -> !masterVariantSkus.contains(skuRequest) ? "No product variant with an sku '" + skuRequest + "' exist." : "")
				.collect(Collectors.joining());
	}
}