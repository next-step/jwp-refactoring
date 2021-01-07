package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.exception.NotFoundException;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private MenuGroupResponse menuGroup;
	private List<MenuProductResponse> menuProducts;

	private MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup, List<MenuProductResponse> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public static MenuResponse of(Menu menu) {
		if (menu == null) {
			throw new NotFoundException("메뉴 정보를 찾을 수 없습니다.");
		}
		List<MenuProductResponse> menuProducts = menu.getMenuProducts()
			.stream()
			.map(MenuProductResponse::of)
			.collect(Collectors.toList());

		return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), MenuGroupResponse.of(menu.getMenuGroup()), menuProducts);
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

	public List<MenuProductResponse> getMenuProducts() {
		return menuProducts;
	}
}
