package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {

	private Long seq;
	private ProductResponse product;
	private Long quantity;

	protected MenuProductResponse() {
	}

	private MenuProductResponse(Long seq, ProductResponse product, Long quantity) {
		this.seq = seq;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(Long seq, Product product, Long quantity) {
		return new MenuProductResponse(seq, product.toResDto(), quantity);
	}

	public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(MenuProduct::toResDto)
			.collect(Collectors.toList());
	}

	public Long getSeq() {
		return seq;
	}

	public ProductResponse getProduct() {
		return product;
	}

	public Long getQuantity() {
		return quantity;
	}
}
