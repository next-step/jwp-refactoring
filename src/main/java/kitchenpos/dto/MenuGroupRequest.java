package kitchenpos.dto;

public class MenuGroupRequest {
	// TODO : http 통신과 규격 맞는지 테스트
	private String name;

	public MenuGroupRequest() {
	}

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
