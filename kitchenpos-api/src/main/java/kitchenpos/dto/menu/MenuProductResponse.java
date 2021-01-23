package kitchenpos.dto.menu;


import kitchenpos.domain.menu.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
	private Long seq;
	private Long productId;
	private Long quantity;

	protected MenuProductResponse() {
	}

	public MenuProductResponse(Long seq, Long productId, Long quantity) {
		this.seq = seq;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProduct().getId(),
				menuProduct.getQuantity());
	}

	public static List<MenuProductResponse> of(List<MenuProduct> saveAll) {
		return saveAll.stream()
				.map(MenuProductResponse::of)
				.collect(Collectors.toList());
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}
