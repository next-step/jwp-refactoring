package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.Objects;

public class MenuGroupRequest {
	private String name;

	public MenuGroupRequest() {
	}

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public MenuGroup toMenuGroup() {
		return new MenuGroup(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MenuGroupRequest that = (MenuGroupRequest) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
