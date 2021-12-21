package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.Price;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Entity
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts = MenuProducts.EMPTY_MENU_PRODUCTS;

	protected Menu() {
	}

	private Menu(String name, Price price, MenuGroup menuGroup) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public static Menu of(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		return new Menu(name, price, menuGroup, MenuProducts.from(menuProducts));
	}

	public static Menu of(String name, Price price, MenuGroup menuGroup) {
		return new Menu(name, price, menuGroup);
	}

	public static Menu of(MenuRequest menuRequest, MenuGroup menuGroup) {
		return new Menu(menuRequest.getName(), Price.from(menuRequest.getPrice()), menuGroup);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price.getPrice();
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(MenuGroup menuGroup) {
		this.menuGroup = menuGroup;
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}

	public MenuResponse toResDto() {
		return MenuResponse.of(id, name, price, menuGroup, menuProducts.getMenuProducts());
	}

	public void addMenuProducts(List<MenuProduct> savedMenuProducts, Integer price) {
		menuProducts.addList(savedMenuProducts, Price.from(price));
	}
}
