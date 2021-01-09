package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.exception.NotFoundException;

public class MenuProductResponse {

	private Long seq;
	private ProductResponse product;
	private long quantity;

	private MenuProductResponse(Long seq, ProductResponse product, long quantity) {
		this.seq = seq;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProductResponse of(MenuProduct menuProduct) {
		if (menuProduct == null) {
			throw new NotFoundException("메뉴 상품 정보를 찾을 수 없습니다.");
		}
		return new MenuProductResponse(menuProduct.getSeq(), ProductResponse.of(menuProduct.getProduct()), menuProduct.getQuantity());
	}

	public Long getSeq() {
		return seq;
	}

	public ProductResponse getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}
}
