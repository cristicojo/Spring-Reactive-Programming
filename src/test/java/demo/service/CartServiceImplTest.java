package demo.service;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartImpl;
import com.commercetools.api.models.cart.LineItem;
import com.commercetools.api.models.cart.LineItemImpl;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringImpl;
import com.commercetools.api.models.product.ProductVariant;
import com.commercetools.api.models.product.ProductVariantImpl;
import demo.acl.adapter.impl.CartAdapterImpl;
import demo.response.CartAttributeResponse;
import demo.response.CartDataResponse;
import demo.response.CartResponse;
import demo.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

	@Mock
	private CartAdapterImpl cartAdapter;

	@InjectMocks
	private CartServiceImpl cartService;


	@Test
	void fetchCartByIdTest() {

		Cart cart = new CartImpl();
		LineItem lineItem= new LineItemImpl();
		ProductVariant productVariant = new ProductVariantImpl();
		LocalizedString localizedString = new LocalizedStringImpl();

		localizedString.setValue("1", "cristi");

		productVariant.setSku("black");
		productVariant.setId(123456L);

		lineItem.setId("123456");
		lineItem.setVariant(productVariant);
		lineItem.setQuantity(12L);
		lineItem.setName(localizedString);

		cart.setLineItems(lineItem);
		cart.setId("69708947375");
		cart.setLocale("na-web");

		Mono<CartResponse> cartResponseMono = Mono.just(CartResponse.builder()
				.data(List.of(CartDataResponse.builder()
						.id("69708947375")
						.type("Cart")
						.attributes(CartAttributeResponse.builder()
								.version(1234L)
								.channel("en-US")
								.locale("na-web")
								.lineItems(List.of(lineItem))
								.build())
						.build()))
				.build());

		doReturn(cartResponseMono).when(cartAdapter).fetchCartByIdAdapter("69708947375", "na-web", "en-US");
		var mono = cartService.fetchCartById("69708947375", "na-web", "en-US");

		StepVerifier.create(mono)
				.expectNextMatches(
						response -> {
							verify(cartAdapter, times(1))
									.fetchCartByIdAdapter("69708947375", "na-web", "en-US");
							return response
									.getData()
									.get(0)
									.getAttributes()
									.getLineItems()
									.get(0)
									.getVariant()
									.getSku()
									.equals("black");
						})
				.verifyComplete();
	}

	@Test
	void fetchCartsTest() {

		Cart cart = new CartImpl();
		LineItem lineItem= new LineItemImpl();
		ProductVariant productVariant = new ProductVariantImpl();
		LocalizedString localizedString = new LocalizedStringImpl();

		localizedString.setValue("1", "cristi");

		productVariant.setSku("black");
		productVariant.setId(123456L);

		lineItem.setId("123456");
		lineItem.setVariant(productVariant);
		lineItem.setQuantity(12L);
		lineItem.setName(localizedString);

		cart.setLineItems(lineItem);
		cart.setId("69708947375");
		cart.setLocale("na-web");

		Mono<ResponseEntity<CartResponse>> cartResponseMono = Mono.just(new ResponseEntity<>(CartResponse.builder()
				.data(List.of(CartDataResponse.builder()
						.id("69708947375")
						.type("Cart")
						.attributes(CartAttributeResponse.builder()
								.version(1234L)
								.channel("en-US")
								.locale("na-web")
								.lineItems(List.of(lineItem))
								.build())
						.build()))
				.build(), HttpStatus.CREATED));

		doReturn(cartResponseMono).when(cartAdapter).fetchCartsAdapter( "na-web", "en-US");
		var mono = cartService.fetchCarts("na-web", "en-US");

		StepVerifier.create(mono)
				.expectNextMatches(
						response -> {
							verify(cartAdapter, times(1))
									.fetchCartsAdapter( "na-web", "en-US");
							return Objects.requireNonNull(response
											.getBody())
									.getData()
									.get(0)
									.getAttributes()
									.getLineItems()
									.get(0)
									.getVariant()
									.getSku()
									.equals("black");
						})
				.verifyComplete();
	}


}
