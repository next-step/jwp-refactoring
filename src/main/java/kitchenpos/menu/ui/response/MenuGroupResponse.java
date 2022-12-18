package kitchenpos.menu.ui.response;

public class MenuGroupResponse {
	private final Long id;
	private final String name;

	private MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupResponse of(Long id, String name) {
		return new MenuGroupResponse(id, name);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
