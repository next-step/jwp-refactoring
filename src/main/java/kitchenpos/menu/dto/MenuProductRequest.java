package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {

	private Long productId;
	private long quantity;

	public MenuProductRequest(Long productId, long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public MenuProduct toMenuProduct(Product product) {
		return new MenuProduct(product, new Quantity(quantity));
	}
}
