package kitchenpos.menu.dto;

import java.util.Objects;
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

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MenuProductResponse that = (MenuProductResponse) o;
		return Objects.equals(seq, that.seq);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq);
	}
}
