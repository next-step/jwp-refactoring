package kitchenpos.web.menu.dto;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
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
}
