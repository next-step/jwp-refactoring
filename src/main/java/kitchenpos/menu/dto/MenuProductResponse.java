package kitchenpos.menu.dto;

import kitchenpos.menu.domain.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {

	private Long id;
	private ProductResponse product;
	private long quantity;

	protected MenuProductResponse() {
	}

	private MenuProductResponse(Long id, ProductResponse product, long quantity) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		return new MenuProductResponse(
			menuProduct.getId(),
			ProductResponse.of(menuProduct.getProduct()),
			menuProduct.getQuantity()
		);
	}

	public Long getId() {
		return id;
	}

	public ProductResponse getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}
}
