package demo.request.badrequest;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductVariant;
import demo.request.CartRequest;
import demo.request.LineItemRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartBadRequest {

	private final ProjectApiRoot projectApiRoot;


	public String reason(CartRequest cartRequest) {

		List<Product> products = projectApiRoot.products().get().execute().join().getBody().getResults();

		var productSkusWithTaxCategoryNotNull = products.stream()
				.filter(result -> Objects.nonNull(result.getTaxCategory()))
				.map(result -> result.getMasterData()
						.getStaged()
						.getMasterVariant()
						.getSku())
				.collect(Collectors.toList());

		List<String> masterVariantSkus = products.stream()
				.map(product -> product.getMasterData().getStaged().getMasterVariant().getSku())
				.collect(Collectors.toList());

		List<String> variantsSkus = products.stream()
				.flatMap(product -> product.getMasterData()
						.getStaged()
						.getVariants()
						.stream()
						.map(ProductVariant::getSku))
				.collect(Collectors.toList());

		masterVariantSkus.addAll(variantsSkus);

		List<String> skusRequest = cartRequest.getData().getAttributes().getLineItems()
				.stream()
				.map(LineItemRequest::getSku)
				.collect(Collectors.toList());

		return skusRequest.stream().map(skuRequest ->
						!masterVariantSkus.contains(skuRequest)
								? "No product variant with an sku '" + skuRequest + "' exist."
								: !productSkusWithTaxCategoryNotNull.contains(skuRequest)
								? "Product with sku: '" + skuRequest + "' does not have a tax category."
								: "")
				.collect(Collectors.joining(" "));
	}
}