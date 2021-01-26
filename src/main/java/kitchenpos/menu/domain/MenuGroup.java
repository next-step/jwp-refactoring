package kitchenpos.menu.domain;


import common.entity.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "menu_group")
public class MenuGroup extends BaseIdEntity {

	@Column(name = "name", nullable = false)
	private String name;

	protected MenuGroup() {
	}

	public MenuGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MenuGroup)) return false;
		if (!super.equals(o)) return false;
		MenuGroup menuGroup = (MenuGroup) o;
		return Objects.equals(name, menuGroup.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name);
	}
}
