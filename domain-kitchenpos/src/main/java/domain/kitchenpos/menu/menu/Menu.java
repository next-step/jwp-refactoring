package domain.kitchenpos.menu.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import domain.kitchenpos.menu.product.Price;

@Entity
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
		this.name = name;
		this.price = new Price(price);
		this.menuGroup = menuGroup;
		this.menuProducts = new MenuProducts();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price.price();
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts.getMenuProducts();
	}

	private static void validatePriceEquals(final BigDecimal price, final MenuProducts menuProducts) {
		BigDecimal sum = menuProducts.totalPrice();
		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException("메뉴 상품의 가격(상품 가격 * 수량)의 합이 메뉴의 가격보다 작습니다.");
		}
	}

	public void addMenuProducts(final List<MenuProduct> menuProducts) {
		this.addMenuProducts(new MenuProducts(menuProducts));
	}

	public void addMenuProducts(final MenuProducts menuProducts) {
		validatePriceEquals(this.getPrice(), menuProducts);
		this.menuProducts = menuProducts;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Menu menu = (Menu)o;
		return Objects.equals(id, menu.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
