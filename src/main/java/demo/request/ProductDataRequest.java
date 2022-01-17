package demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDataRequest {

	@NotBlank
	private ProductVariantRequest masterVariant;
	@NotBlank
	private ProductVariantRequest variant;

}
