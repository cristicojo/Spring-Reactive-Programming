package demo.mapper;

import com.commercetools.api.models.cart.Cart;
import demo.response.CartDataResponse;
import demo.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CartMapper {

	CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);


	default CartResponse cartListToCartResponse(List<Cart> cartList, String locale, String channel) {
		return cartList.isEmpty() ? null : CartResponse.builder().data(cartListToCartDataResponseList(cartList, locale, channel)).build();
	}


	default List<CartDataResponse> cartListToCartDataResponseList(List<Cart> cartList, String locale, String channel) {

		List<CartDataResponse> dataList = cartList.stream().map(this::cartToCartDataResponse).collect(Collectors.toList());

		return dataList.stream().map(data -> {
			data.setType("Cart");
			data.getAttributes().setLocale(locale);
			data.getAttributes().setChannel(channel);
			return data;
		}).collect(Collectors.toList());
	}

	@Mapping(target = "attributes.version", source = "cart.version")
	@Mapping(target = "attributes.createdAt", source = "cart.createdAt")
	@Mapping(target = "attributes.lastModifiedAt", source = "cart.lastModifiedAt")
	@Mapping(target = "attributes.lastModifiedBy", source = "cart.lastModifiedBy")
	@Mapping(target = "attributes.createdBy", source = "cart.createdBy")
	@Mapping(target = "attributes.lineItems", source = "cart.lineItems")
	@Mapping(target = "attributes.totalPrice", source = "cart.totalPrice")
	CartDataResponse cartToCartDataResponse(Cart cart);

}