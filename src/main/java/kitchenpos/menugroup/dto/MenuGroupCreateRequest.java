package kitchenpos.menugroup.dto;

import kitchenpos.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupName;

public class MenuGroupCreateRequest {
	private String name;

	public MenuGroupCreateRequest() {
	}

	public MenuGroupCreateRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuGroup toMenuGroup() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName(name);
		return menuGroup;
	}

	public kitchenpos.menugroup.domain.MenuGroup toToBeMenuGroup() {
		MenuGroupName name = MenuGroupName.of(this.name);
		return kitchenpos.menugroup.domain.MenuGroup.of(name);
	}
}
