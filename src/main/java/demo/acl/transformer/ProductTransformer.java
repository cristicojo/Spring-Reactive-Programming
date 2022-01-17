package demo.acl.transformer;

import com.commercetools.api.models.product.Product;
import demo.mapper.ProductMapper;
import demo.response.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductTransformer {


	public ProductResponse transformToProductResponse(List<Product> productList) {

		ProductResponse productResponse = ProductMapper.INSTANCE.productToProductResponse(productList);
		log.info(productList.size() == 1 ? "Product has been transformed to ProductResponse"
				: "productList has been transformed to ProductResponse");

		return productResponse;
	}
}