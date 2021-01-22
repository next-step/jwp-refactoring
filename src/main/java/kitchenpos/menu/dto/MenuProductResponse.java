package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
	private final Long seq;
	private final Long productId;
	private final long quantity;

	public MenuProductResponse(final Long seq, final Long productId, final long quantity) {
		this.seq = seq;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(Long seq, Long id, long quantity) {
		return new MenuProductResponse(seq, id, quantity);
	}

	public static MenuProductResponse of(final MenuProduct menuProduct) {
		return of(menuProduct.getId(), menuProduct.getProduct().getId(),
			menuProduct.getQuantity());
	}

	public Long getSeq() {
		return seq;
	}

	public Long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}
}
