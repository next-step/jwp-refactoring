package kitchenpos.menugroup.domain;

public class MenuGroupResponse {

	private Long id;
	private String name;

	protected MenuGroupResponse() {
	}

	private MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupResponse of(MenuGroup menuGroup) {
		return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
