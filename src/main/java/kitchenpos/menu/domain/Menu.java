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
import javax.persistence.Table;

import kitchenpos.common.Price;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
@Table(name = "menu")
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		validate(price, menuProducts);
		this.id = id;
		this.name = name;
		this.price = Price.of(price);
		this.menuGroup = menuGroup;
		this.menuProducts = MenuProducts.of(this, menuProducts);
	}

	public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		return of(null, name, price, menuGroup, menuProducts);
	}

	public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		return new Menu(id, name, price, menuGroup, menuProducts);
	}

	private void validate(BigDecimal price, List<MenuProduct> menuProducts) {
		final BigDecimal sumMenuProductsPrice = menuProducts.stream()
			.map(MenuProduct::getTotalPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		if (price.compareTo(sumMenuProductsPrice) > 0) {
			throw new InvalidMenuPriceException(price, sumMenuProductsPrice);
		}
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

	public List<MenuProduct> getMenuProducts() {
		return menuProducts.getMenuProducts();
	}
}
