package demo.mapper;

import com.commercetools.api.models.product.Product;
import demo.response.ProductDataResponse;
import demo.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	default ProductResponse productToProductResponse(List<Product> productList) {
		return productList.isEmpty() ? null : ProductResponse.builder().data(productListToProductDataResponseList(productList)).build();
	}


	default List<ProductDataResponse> productListToProductDataResponseList(List<Product> productList) {
		return productList.stream().map(this::productToProductDataResponse).collect(Collectors.toList());
	}


	@Mapping(target = "attributes.version", source = "product.version")
	@Mapping(target = "attributes.createdAt", source = "product.createdAt")
	@Mapping(target = "attributes.lastModifiedAt", source = "product.lastModifiedAt")
	@Mapping(target = "attributes.lastModifiedBy", source = "product.lastModifiedBy")
	@Mapping(target = "attributes.createdBy", source = "product.createdBy")
	@Mapping(target = "attributes.key", source = "product.key")
	@Mapping(target = "attributes.productType", source = "product.productType")
	@Mapping(target = "attributes.masterData", source = "product.masterData")
	@Mapping(target = "attributes.taxCategory", source = "product.taxCategory")
	@Mapping(target = "attributes.state", source = "product.state")
	@Mapping(target = "attributes.reviewRatingStatistics", source = "product.reviewRatingStatistics")
	ProductDataResponse productToProductDataResponse(Product product);
}
