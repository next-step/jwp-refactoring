package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Name;

@Table(name = "menu_group")
@Entity
public class MenuGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Name name;

	protected MenuGroup() {
	}

	private MenuGroup(Long id, Name name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroup of(Long id, Name name) {
		return new MenuGroup(id, name);
	}

	public static MenuGroup of(Long id, String name) {
		return new MenuGroup(id, Name.of(name));
	}

	public static MenuGroup create(String name) {
		return new MenuGroup(null, Name.of(name));
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}
}
