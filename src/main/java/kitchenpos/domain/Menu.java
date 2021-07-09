package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
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

	@Column(name = "name")
	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id", nullable = false)
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	protected Menu() {
	}

	public Menu(String name, Price price, MenuGroup menuGroup) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	public Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProductList) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;

		for (MenuProduct menuProduct : menuProductList) {
			addMenuProduct(menuProduct);
		}
		validateMenuPrice();
	}

	public void addMenuProduct(MenuProduct menuProduct) {
		this.menuProducts.addMenuProduct(menuProduct);
		menuProduct.changeMenu(this);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts.getMenuProducts();
	}

	public void validateMenuPrice() {
		Price totalPrice = menuProducts.getTotalPrice();

		if (this.price.compareTo(totalPrice) > 0) {
			throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격의 합보다 작아야합니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Menu menu = (Menu)o;
		return Objects.equals(id, menu.id) && Objects.equals(name, menu.name)
			&& Objects.equals(price, menu.price) && Objects.equals(menuGroup.getId(), menu.menuGroup.getId())
			&& Objects.equals(menuProducts, menu.menuProducts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price, menuGroup, menuProducts);
	}
}
