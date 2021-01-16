package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
	private Long seq;
	private Long productId;
	private Long quantity;

	public MenuProductResponse() {
	}

	public MenuProductResponse(Long seq, Long productId, Long quantity) {
		this.seq = seq;
		this.productId = productId;
		this.quantity = quantity;
	}

	public MenuProductResponse(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProduct().getId(),
				menuProduct.getQuantity());
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}
