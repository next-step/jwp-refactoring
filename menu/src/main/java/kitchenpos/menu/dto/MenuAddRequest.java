package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.domain.Menu;
import kitchenpos.menu.domain.domain.MenuProduct;
import kitchenpos.menugroup.domain.domain.MenuGroup;

public class MenuAddRequest {

	private String name;

	private BigDecimal price;

	private Long menuGroupId;

	private List<MenuProductAddRequest> menuProductAddRequests;

	protected MenuAddRequest() {
	}

	private MenuAddRequest(String name, BigDecimal price, Long menuGroupId,
		List<MenuProductAddRequest> menuProductAddRequests) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProductAddRequests = menuProductAddRequests;
	}

	public static MenuAddRequest of(String name, BigDecimal price, Long menuGroupId,
		List<MenuProductAddRequest> menuProductAddRequests) {
		return new MenuAddRequest(name, price, menuGroupId, menuProductAddRequests);
	}

	public Menu toEntity(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		return Menu.of(name, price, menuGroup, menuProducts);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductAddRequest> getMenuProductAddRequests() {
		return menuProductAddRequests;
	}
}
