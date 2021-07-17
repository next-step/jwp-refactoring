package kitchenpos.menu.dto;

import java.util.List;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuResponse {
	private Long id;
	private String name;
	private Price price;
	private MenuGroup MenuGroup;
	private List<MenuProduct> menuProducts;
}
