package demo.response;

import com.commercetools.api.models.common.CreatedBy;
import com.commercetools.api.models.common.LastModifiedBy;
import com.commercetools.api.models.product.ProductCatalogData;
import com.commercetools.api.models.product_type.ProductTypeReference;
import com.commercetools.api.models.review.ReviewRatingStatistics;
import com.commercetools.api.models.state.StateReference;
import com.commercetools.api.models.tax_category.TaxCategoryReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeResponse {

	private Long version;
	private ZonedDateTime createdAt;
	private ZonedDateTime lastModifiedAt;
	private LastModifiedBy lastModifiedBy;
	private CreatedBy createdBy;
	private String key;
	private ProductTypeReference productType;
	private ProductCatalogData masterData;
	private TaxCategoryReference taxCategory;
	private StateReference state;
	private ReviewRatingStatistics reviewRatingStatistics;

}
