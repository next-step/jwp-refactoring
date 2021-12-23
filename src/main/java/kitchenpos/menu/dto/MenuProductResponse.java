package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;

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

	public static MenuProductResponse of(Long seq, Long productId, Long quantity) {
		return new MenuProductResponse(seq, productId, quantity);
	}

	public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProduct::toResDto)
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
