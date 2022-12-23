package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu_group")
public class MenuGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	protected MenuGroup() {
	}

	public MenuGroup(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public MenuGroup(String name) {
		this(null, name);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MenuGroup menuGroup = (MenuGroup)o;
		return id.equals(menuGroup.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
