package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuProductResponse {

	private Long seq;
	private Long productId;
	private Long quantity;

	protected MenuProductResponse() {
	}

	private MenuProductResponse(Long seq, Long productId, Long quantity) {
		this.seq = seq;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse from(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
	}

	public static MenuProductResponse of(Long seq, Long productId, Long quantity) {
		return new MenuProductResponse(seq, productId, quantity);
	}

	public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProductResponse::from)
			.collect(Collectors.toList());
	}

	public static List<MenuProductResponse> ofList(MenuProducts menuProducts) {
		return menuProducts.getMenuProducts()
			.stream()
			.map(MenuProductResponse::from)
			.collect(Collectors.toList());
	}

	public Long getSeq() {
		return seq;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
