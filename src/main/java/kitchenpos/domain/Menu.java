package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@Embedded
	private MenuPrice price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	protected Menu() {
	}

	private Menu(String name, BigDecimal price, MenuGroup menuGroup) {
		this.name = name;
		this.price = MenuPrice.of(price);
		this.menuGroup = menuGroup;
	}

	public static Menu create(String name, BigDecimal price, MenuGroup menuGroup) {
		return new Menu(name, price, menuGroup);
	}

	public void addMenuProduct(MenuProduct menuProduct) {
		this.menuProducts.add(menuProduct);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MenuPrice getPrice() {
		return price;
	}

	public BigDecimal getRealPrice() {
		return price.getPrice();
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts.getMenuProducts();
	}
}
