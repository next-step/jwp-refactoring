package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Price;
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

	@Column(nullable = false)
	private Long menuGroupId;

	@Embedded
	private MenuProducts menuProducts = MenuProducts.EMPTY_MENU_PRODUCTS;

	protected Menu() {
	}

	private Menu(String name, Price price, Long menuGroupId) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
	}

	private Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public static Menu from(MenuRequest menuRequest) {
		return new Menu(menuRequest.getName(), Price.from(menuRequest.getPrice()), menuRequest.getMenuGroupId());
	}

	public static Menu of(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
		return new Menu(name, price, menuGroupId, MenuProducts.from(menuProducts));
	}

	public static Menu of(String name, Price price, Long menuGroupId) {
		return new Menu(name, price, menuGroupId);
	}

	public static Menu of(MenuRequest menuRequest, Long menuGroupId) {
		return new Menu(menuRequest.getName(), Price.from(menuRequest.getPrice()), menuGroupId);
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

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}

	public MenuResponse toResDto() {
		return MenuResponse.of(id, name, price, menuGroupId, menuProducts.getMenuProducts());
	}

	public void addMenuProducts(List<MenuProduct> savedMenuProducts) {
		menuProducts.addList(savedMenuProducts);
	}
}
