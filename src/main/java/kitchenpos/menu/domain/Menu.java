package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Table(name = "menu")
@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Name name;

	@Embedded
	private Price price;

	private Long menuGroupId;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	public static Menu of(Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
		throwOnPriceInvalid(price, menuProducts);

		Menu menu = new Menu();
		menu.name = name;
		menu.price = price;
		menu.menuGroupId = menuGroupId;
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

	public Name getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}
}
