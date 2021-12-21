package kitchenpos.menugroup.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.domain.Value;

@Embeddable
public class MenuGroupName extends Value<MenuGroupName> {
	@Column(name = "name", nullable = false)
	private String value;

	protected MenuGroupName() {
	}

	public static MenuGroupName of(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("이름은 빈 값일 수 없습니다.");
		}

		MenuGroupName menuGroupName = new MenuGroupName();
		menuGroupName.value = name;
		return menuGroupName;
	}

	public String getValue() {
		return value;
	}
}
