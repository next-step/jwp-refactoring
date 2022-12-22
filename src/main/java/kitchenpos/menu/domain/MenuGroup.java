package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	protected MenuGroup() {
	}

	private MenuGroup(String name) {
		this.name = name;
	}

	public static MenuGroup from(String name) {
		return new MenuGroup(name);
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		MenuGroup menuGroup = (MenuGroup)o;
		return Objects.equals(name, menuGroup.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}
}
