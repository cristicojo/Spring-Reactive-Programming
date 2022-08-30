package demo.acl.facade;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartDraft;
import com.commercetools.api.models.cart.CartPagedQueryResponse;
import demo.request.CartRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CartFacade {

	Cart fetchCartByIdFacade(String cartId);

	List<Cart> fetchCartsFacade();

	CompletableFuture<ApiHttpResponse<Cart>> saveCartFacade(CartDraft cartDraft);

	CompletableFuture<ApiHttpResponse<Cart>> saveCartWithLineItemsFacade(CartDraft cartDraft);

	CompletableFuture<ApiHttpResponse<Cart>> saveLineItemFacade(String cartId, CartRequest cartRequest);

	CompletableFuture<ApiHttpResponse<Cart>> removeLineItemFacade(String cartId, CartRequest cartRequest);

	CompletableFuture<ApiHttpResponse<Cart>> setLineItemQuantityFacade(String cartId, CartRequest cartRequest);
}
