package kitchenpos.menu.dto;

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
}
