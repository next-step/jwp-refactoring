package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.Name;
import kitchenpos.common.Price;

@Entity
public class Menu {
	public static String ENTITY_NAME = "메뉴";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Name name;
	private Price price;
	private Long menuGroupId;
	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	protected Menu() {}

	private Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
		menuProducts.updateMenu(this);
		this.name = Name.of(name);
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public static Menu of(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
		return new Menu(name, Price.of(price), menuGroupId, menuProducts);
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

	public boolean moreExpensive(Price menuProductsPrice) {
		return price.compareTo(menuProductsPrice) > 0;
	}
}
