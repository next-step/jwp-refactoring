package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.Value;

@Embeddable
public class MenuProductQuantity extends Value<MenuProductQuantity> {
	@Column(name = "quantity", nullable = false)
	private long value;

	protected MenuProductQuantity() {
	}

	public static MenuProductQuantity of(Long quantity) {
		if (quantity == null || quantity < 0) {
			throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
		}

		MenuProductQuantity menuProductQuantity = new MenuProductQuantity();
		menuProductQuantity.value = quantity;
		return menuProductQuantity;
	}

	public long getValue() {
		return value;
	}
}
