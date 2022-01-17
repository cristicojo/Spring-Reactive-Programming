package demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemRequest {

	@NotBlank
	private String id;
	@NotBlank
	private String sku;
	@NotNull
	private Long quantity;
	@NotBlank
	private MoneyRequest externalPrice;
}
