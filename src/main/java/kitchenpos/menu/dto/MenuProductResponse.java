package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}
