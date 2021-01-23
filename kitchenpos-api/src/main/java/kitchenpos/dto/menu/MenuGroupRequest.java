package kitchenpos.dto.menu;


import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {

	private String name;

	public MenuGroup toEntity() {
		return new MenuGroup(this.name);
	}

	protected MenuGroupRequest() {
	}
	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
