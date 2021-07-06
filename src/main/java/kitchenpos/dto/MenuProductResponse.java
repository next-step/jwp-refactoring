package kitchenpos.dto;

import java.util.Objects;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
	private Long seq;
	private Long menuId;
	private ProductResponse product;
	private long quantity;

	protected MenuProductResponse() {
	}

	public MenuProductResponse(Long seq, Long menuId, ProductResponse product, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		Long menuId = menuProduct.getMenu().getId();
		ProductResponse productResponse = ProductResponse.of(menuProduct.getProduct());
		return new MenuProductResponse(menuProduct.getSeq(), menuId, productResponse,
			menuProduct.getQuantity());
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menuId;
	}

	public ProductResponse getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MenuProductResponse that = (MenuProductResponse)o;
		return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menuId,
			that.menuId) && Objects.equals(product.getId(), that.product.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq, menuId, product, quantity);
	}
}
