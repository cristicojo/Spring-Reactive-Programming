package demo.acl.facade;

import com.commercetools.api.client.ByProjectKeyCartsByIDGet;
import com.commercetools.api.client.ByProjectKeyCartsByIDRequestBuilder;
import com.commercetools.api.client.ByProjectKeyCartsGet;
import com.commercetools.api.client.ByProjectKeyCartsRequestBuilder;
import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartImpl;
import com.commercetools.api.models.cart.CartPagedQueryResponse;
import com.commercetools.api.models.cart.LineItem;
import com.commercetools.api.models.cart.LineItemImpl;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringImpl;
import com.commercetools.api.models.product.ProductVariant;
import com.commercetools.api.models.product.ProductVariantImpl;
import demo.acl.facade.impl.CartFacadeImpl;
import io.vrap.rmf.base.client.ApiHttpResponse;
import io.vrap.rmf.base.client.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartFacadeImplTest {

	@Mock
	private ProjectApiRoot projectApiRoot;

	@Mock
	private ByProjectKeyCartsRequestBuilder byProjectKeyCustomersRequestBuilder;

	@Mock
	private ByProjectKeyCartsByIDRequestBuilder byProjectKeyCartsByIDRequestBuilder;

	@Mock
	private ByProjectKeyCartsByIDGet byProjectKeyCartsByIDGet;

	@Mock
	private ByProjectKeyCartsGet byProjectKeyCartsGet;

	@Mock
	private CompletableFuture<ApiHttpResponse<Cart>> future;

	@Mock
	private CompletableFuture<ApiHttpResponse<CartPagedQueryResponse>> futureCartPagedQueryResponse;

	@Mock
	private CompletableFuture<Cart> futureCart;

	@Mock
	private CompletableFuture<List<Cart>> futureCartList;

	@InjectMocks
	private CartFacadeImpl cartFacade;


	@Test
	void shouldThrowExceptionWhenFetchCartByIdFacade() {

		when(projectApiRoot.carts()).thenReturn(byProjectKeyCustomersRequestBuilder);
		doThrow(new CompletionException("The cart with id: 69708947375 was not found.", new NotFoundException(404, null, null, null, null)))
				.when(byProjectKeyCustomersRequestBuilder).withId("69708947375");

		assertThrows(ResponseStatusException.class, () -> cartFacade.fetchCartByIdFacade("69708947375"));
	}

	@Test
	void shouldReturnCartWhenFetchCartByIdFacade () {

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

		doReturn(byProjectKeyCustomersRequestBuilder).when(projectApiRoot).carts();
		doReturn(byProjectKeyCartsByIDRequestBuilder).when(byProjectKeyCustomersRequestBuilder).withId("69708947375");
		doReturn(byProjectKeyCartsByIDGet).when(byProjectKeyCartsByIDRequestBuilder).get();
		doReturn(future).when(byProjectKeyCartsByIDGet).execute();
		doReturn(futureCart).when(future).thenApplyAsync(any());
		doReturn(cart).when(futureCart).join();

		assertEquals(cartFacade.fetchCartByIdFacade("69708947375")
				.getLineItems().get(0).getVariant().getSku(), "black");
	}

	@Test
	void fetchCartsFacadeTest() {

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

		doReturn(byProjectKeyCustomersRequestBuilder).when(projectApiRoot).carts();
		doReturn(byProjectKeyCartsGet).when(byProjectKeyCustomersRequestBuilder).get();
		doReturn(futureCartPagedQueryResponse).when(byProjectKeyCartsGet).execute();
		doReturn(futureCartList).when(futureCartPagedQueryResponse).thenApplyAsync(any());
		doReturn(List.of(cart)).when(futureCartList).join();

		assertEquals(cartFacade.fetchCartsFacade().get(0).getLineItems().get(0).getVariant().getSku(), "black");

	}
}
