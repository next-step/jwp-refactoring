package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.domain.Value;

@Embeddable
public class MenuName extends Value<MenuName> {
	@Column(name = "name", nullable = false)
	private String value;

	protected MenuName() {
	}

	public static MenuName of(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("이름은 빈 값일 수 없습니다.");
		}

		MenuName menuName = new MenuName();
		menuName.value = name;
		return menuName;
	}

	public String getValue() {
		return value;
	}
}
