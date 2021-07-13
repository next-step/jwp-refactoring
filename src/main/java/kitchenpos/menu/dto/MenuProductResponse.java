package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuProductResponse {
	private Long seq;
	private ProductResponse productResponse;
	private Long quantity;

	public MenuProductResponse(Long seq, ProductResponse productResponse,
		Long quantity) {
		this.seq = seq;
		this.productResponse = productResponse;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public ProductResponse getProductResponse() {
		return productResponse;
	}

	public Long getQuantity() {
		return quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq(), ProductResponse.of(menuProduct.getProduct()),
			menuProduct.getQuantity().value());
	}

	public static List<MenuProductResponse> of(MenuProducts menuProducts) {
		return menuProducts.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());
	}
}
