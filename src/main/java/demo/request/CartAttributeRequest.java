package demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartAttributeRequest {

	@NotBlank
	private String currency;
	@NotBlank
	private String customerId;
	@NotBlank
	private List<LineItemRequest> lineItems;
}
