package demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantRequest {

	@NotNull
	private Long id;
	@NotBlank
	private String sku;
	@NotBlank
	private String key;
}
