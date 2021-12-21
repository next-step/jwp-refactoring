package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;

@Table(name = "menu")
@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private MenuName name;

	@Embedded
	private Price price;

	@ManyToOne(optional = false)
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	public static Menu of(MenuName name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		throwOnPriceInvalid(price, menuProducts);

		Menu menu = new Menu();
		menu.name = name;
		menu.price = price;
		menu.menuGroup = menuGroup;
		menu.menuProducts = menuProducts;
		return menu;
	}

	private static void throwOnPriceInvalid(Price price, MenuProducts menuProducts) {
		if (price.compareTo(menuProducts.getTotalPrice()) > 0) {
			throw new IllegalArgumentException("메뉴의 가격은 메뉴상품들의 전체 가격보다 적거나 같아야 합니다.");
		}
	}

	public Long getId() {
		return id;
	}

	public MenuName getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}
}
