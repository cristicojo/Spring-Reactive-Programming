package demo.acl.transformer;

import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.MoneyBuilder;
import demo.mapper.CartMapper;
import demo.request.CartRequest;
import demo.request.LineItemRequest;
import demo.response.CartResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CartTransformer {


	public CartResponse transformToCartResponse(List<Cart> cartList, String locale, String channel) {

		CartResponse cartResponse = CartMapper.INSTANCE.cartListToCartResponse(cartList, locale, channel);
		log.info(cartList.size() == 1 ? "Cart has been transformed to CartResponse" : "cartList has been transformed to CartResponse");
		return cartResponse;
	}


	public CartDraft transformToCartDraft(CartRequest cartRequest, String locale) {

		CartDraft cartDraft = CartDraftBuilder.of()
				.currency(cartRequest.getData().getAttributes().getCurrency())
				.customerId(cartRequest.getData().getAttributes().getCustomerId())
				.locale(locale)
				.build();

		log.info("CartRequest has been transformed to CartDraft");
		return cartDraft;
	}


	public CartDraft transformToCartDraftWithLineItems(CartRequest cartRequest, String locale) {

		var cartDraft = CartDraftBuilder.of()
				.currency(cartRequest.getData().getAttributes().getCurrency())
				.customerId(cartRequest.getData().getAttributes().getCustomerId())
				.locale(locale)
				.lineItems(lineItemDraftBuilder(cartRequest))
				.build();

		log.info("CartRequest containing line items has been transformed to CartDraft containing line items");
		return cartDraft;
	}


	private List<LineItemDraft> lineItemDraftBuilder(CartRequest cartRequest) {

		List<LineItemRequest> lineItemRequestList = cartRequest.getData().getAttributes().getLineItems();

		return lineItemRequestList.stream().map(lineItemRequest -> LineItemDraftBuilder.of()
						.sku(lineItemRequest.getSku())
						.quantity(lineItemRequest.getQuantity())
						.externalPrice(MoneyBuilder.of()
								.currencyCode(lineItemRequest.getExternalPrice().getCurrencyCode())
								.centAmount(lineItemRequest.getExternalPrice().getCentAmount())
								.build())
						.build())
				.collect(Collectors.toList());
	}
}