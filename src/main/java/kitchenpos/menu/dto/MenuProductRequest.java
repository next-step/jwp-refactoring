package kitchenpos.menu.dto;

import java.util.Objects;

public class MenuProductRequest {
	private Long productId;
	private Long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(Long productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MenuProductRequest that = (MenuProductRequest) o;
		return Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, quantity);
	}
}
