package kitchenpos.menugroup.domain;

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

	public static MenuGroup of(Name name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.name = name;
		return menuGroup;
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}
}
