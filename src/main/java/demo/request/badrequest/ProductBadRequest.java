package demo.request.badrequest;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductVariant;
import demo.request.ProductRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductBadRequest {

	@Autowired
	private ProjectApiRoot projectApiRoot;


	public String reason(String productId, ProductRequest productRequest) {

		Product productResponse = projectApiRoot.products().withId(productId).get().execute().join().getBody();

		// get id, key and sku from ProductRequest
		Long idRequest = productRequest.getData().getAttributes().getMasterData().getCurrent().getMasterVariant().getId();
		String keyRequest = productRequest.getData().getAttributes().getMasterData().getCurrent().getMasterVariant().getKey();
		String skuRequest = productRequest.getData().getAttributes().getMasterData().getCurrent().getMasterVariant().getSku();

		// create a list of all ids from variants and id from MasterVariant
		Long idMasterVariantResponse = productResponse.getMasterData().getStaged().getMasterVariant().getId();
		List<Long> variantIds = productResponse.getMasterData().getStaged().getVariants().stream().map(ProductVariant::getId).collect(Collectors.toList());
		variantIds.add(idMasterVariantResponse);

		// create a list of all keys
		String keyMasterVariantResponse = productResponse.getMasterData().getStaged().getMasterVariant().getKey();
		List<String> keys = productResponse.getMasterData().getStaged().getVariants().stream().map(ProductVariant::getKey).collect(Collectors.toList());
		keys.add(keyMasterVariantResponse);

		// create a list of all ProductVariant objects
		ProductVariant productMasterVariantResponse = productResponse.getMasterData().getStaged().getMasterVariant();
		List<ProductVariant> productVariants = productResponse.getMasterData().getStaged().getVariants();
		productVariants.add(productMasterVariantResponse);


		if (variantIds.stream().noneMatch(variantId -> Objects.equals(idRequest, variantId))) {
			return "masterVariantId: Invalid UUID: '" + idRequest + "'";
		}

		if (Objects.nonNull(skuRequest)) {

			// create a list of all skus
			String skuMasterVariantResponse = productResponse.getMasterData().getStaged().getMasterVariant().getSku();
			List<String> skus = productResponse.getMasterData().getStaged().getVariants().stream().map(ProductVariant::getSku).collect(Collectors.toList());
			skus.add(skuMasterVariantResponse);

			if (skus.stream().anyMatch(sku -> StringUtils.equals(skuRequest, sku))) {
				return "A duplicate value '" + skuRequest + "' exists for field 'sku' on one product variant.";
			}

			if (productVariants.stream().anyMatch(productVariant -> Objects.nonNull(productVariant.getAvailability()))) {
				return "The SKU '" + skuRequest + "' cannot be changed because an inventory entry exists.";
			}
		}

		return "A duplicate value '" + keyRequest + "' exists for field 'key' on one product variant.";
	}
}