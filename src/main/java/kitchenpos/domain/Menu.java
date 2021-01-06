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
	private BigDecimal price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	protected Menu() {
	}

	private Menu(String name, BigDecimal price, MenuGroup menuGroup, List<Product> products, List<Long> quantities) {
		addMenuProducts(price, products, quantities);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	public static Menu create(String name, BigDecimal price, MenuGroup menuGroup, List<Product> products, List<Long> quantities) {
		return new Menu(name, price, menuGroup, products, quantities);
	}

	private void addMenuProducts(BigDecimal price, List<Product> products, List<Long> quantities) {
		this.menuProducts.add(this, price, products, quantities);
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

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts.getMenuProducts();
	}
}
