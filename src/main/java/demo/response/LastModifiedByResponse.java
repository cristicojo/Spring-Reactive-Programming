package demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastModifiedByResponse {

	private String clientId;
	private Boolean isPlatformClient = false;

}
