package kitchenpos.menugroup.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private MenuGroupName name;

	protected MenuGroup() {
	}

	public static MenuGroup of(MenuGroupName name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.name = name;
		return menuGroup;
	}

	public Long getId() {
		return id;
	}

	public MenuGroupName getName() {
		return name;
	}
}
