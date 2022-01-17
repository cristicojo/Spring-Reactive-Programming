package demo.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDataResponse {

	private String type;
	private String id;
	private CartAttributeResponse attributes;

}
