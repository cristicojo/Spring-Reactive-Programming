package demo.response;

import com.commercetools.api.models.cart.LineItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartAttributeResponse {

	private Long version;
	private String locale;
	private String channel;
	private ZonedDateTime createdAt;
	private ZonedDateTime lastModifiedAt;
	private LastModifiedByResponse lastModifiedBy;
	private CreatedByResponse createdBy;
	private List<LineItem> lineItems;
	private TypedMoneyResponse totalPrice;
}
