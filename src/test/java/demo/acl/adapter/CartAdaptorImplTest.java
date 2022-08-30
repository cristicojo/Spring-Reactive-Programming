package demo.acl.adapter;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartImpl;
import com.commercetools.api.models.cart.LineItem;
import com.commercetools.api.models.cart.LineItemImpl;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringImpl;
import com.commercetools.api.models.product.ProductVariant;
import com.commercetools.api.models.product.ProductVariantImpl;
import demo.acl.adapter.impl.CartAdapterImpl;
import demo.acl.facade.impl.CartFacadeImpl;
import demo.acl.transformer.CartTransformer;
import demo.response.CartAttributeResponse;
import demo.response.CartDataResponse;
import demo.response.CartResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CartAdaptorImplTest {

	@Mock
	private CartFacadeImpl cartFacade;

	@Mock
	private CartTransformer cartTransformer;

	@InjectMocks
	private CartAdapterImpl cartAdapter;

	@Test
	void fetchCartByIdAdapterTest () {

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

		CartResponse cartResponse = CartResponse.builder()
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
				.build();

		doReturn(cart).when(cartFacade).fetchCartByIdFacade("69708947375");
		doReturn(cartResponse).when(cartTransformer).transformToCartResponse(List.of(cart), "na-web", "en-US");
		Mono<CartResponse> mono = cartAdapter.fetchCartByIdAdapter("69708947375", "na-web", "en-US");

		StepVerifier.create(mono)
				.expectNextMatches(
						response -> {
							Mockito.verify(cartFacade).fetchCartByIdFacade(anyString());
							Mockito.verify(cartTransformer).transformToCartResponse(List.of(cart), "na-web", "en-US");
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
	void fetchCartsAdapterTest(){

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

		CartResponse cartResponse = CartResponse.builder()
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
				.build();

		doReturn(List.of(cart)).when(cartFacade).fetchCartsFacade();
		doReturn(cartResponse).when(cartTransformer).transformToCartResponse(List.of(cart), "na-web", "en-US");

		Mono<ResponseEntity<CartResponse>> mono = cartAdapter.fetchCartsAdapter( "na-web", "en-US");

		StepVerifier.create(mono)
				.expectNextMatches(
						response -> {
							Mockito.verify(cartFacade).fetchCartsFacade();
							Mockito.verify(cartTransformer).transformToCartResponse(List.of(cart), "na-web", "en-US");
							return Objects.requireNonNull(response.getBody())
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
