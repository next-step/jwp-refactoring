package kitchenpos.dto;

import java.math.BigDecimal;

import kitchenpos.domain.Menu;
import kitchenpos.exception.NotFoundException;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private MenuGroupResponse menuGroup;

	private MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	public static MenuResponse of(Menu menu) {
		if (menu == null) {
			throw new NotFoundException("메뉴 정보를 찾을 수 없습니다.");
		}
		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), MenuGroupResponse.of(menu.getMenuGroup()));
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public MenuGroupResponse getMenuGroup() {
		return menuGroup;
	}
}
