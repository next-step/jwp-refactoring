package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {
	private Long seq;
	private Long productId;
	private long quantity;

	public MenuProductDto() {
	}

	public MenuProductDto(Long seq, Long productId, long quantity) {
		this.seq = seq;
		this.productId = productId;
		this.quantity = quantity;
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

	public static MenuProductDto of(MenuProduct menuProduct) {
		MenuProductDto dto = new MenuProductDto();
		dto.seq = menuProduct.getSeq();
		dto.productId = menuProduct.getProduct().getId();
		dto.quantity = menuProduct.getQuantity().getValue();
		return dto;
	}
}
