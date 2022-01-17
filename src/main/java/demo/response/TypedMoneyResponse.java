package demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypedMoneyResponse {

	private Long centAmount;
	private String currencyCode;
}
