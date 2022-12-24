package ktichenpos.menu.menu.ui.response;

import java.util.List;
import java.util.stream.Collectors;

import ktichenpos.menu.menu.domain.MenuProduct;
import ktichenpos.menu.menu.domain.MenuProducts;

public class MenuProductResponse {

	private long seq;
	private long productId;
	private long quantity;

	private MenuProductResponse() {
	}

	private MenuProductResponse(long seq, long productId, long quantity) {
		this.seq = seq;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(long seq, long productId, long quantity) {
		return new MenuProductResponse(seq, productId, quantity);
	}

	public static List<MenuProductResponse> listFrom(MenuProducts menuProducts) {
		return menuProducts.list().stream()
			.map(MenuProductResponse::from)
			.collect(Collectors.toList());
	}

	private static MenuProductResponse from(MenuProduct menuProduct) {
		return MenuProductResponse.of(
			menuProduct.seq(),
			menuProduct.productId(),
			menuProduct.quantity()
		);
	}

	public long getSeq() {
		return seq;
	}

	public long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}
}

