package kitchenpos.domain.menu;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class MenuName {

	private String name;

	protected MenuName() {
	}

	public MenuName(String name) {
		this.name = name;
	}

	public String value() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MenuName menuName = (MenuName)o;
		return name.equals(menuName.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
