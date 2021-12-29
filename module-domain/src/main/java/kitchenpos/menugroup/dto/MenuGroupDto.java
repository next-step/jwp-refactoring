package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupDto {
	private Long id;
	private String name;

	public MenuGroupDto() {
	}

	public MenuGroupDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupDto from(MenuGroup menuGroup) {
		MenuGroupDto dto = new MenuGroupDto();
		dto.id = menuGroup.getId();
		dto.name = menuGroup.getName().getValue();
		return dto;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
