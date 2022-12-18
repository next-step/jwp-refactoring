package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.menu.exception.InvalidMenuPriceException;

@Entity
@Table(name = "menu")
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Name name;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "price"))
	private Money price;

	private Long menuGroupId;

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected Menu() {
	}

	public Menu(String name2, Long price, Long menuGroupId, Map<Product, Integer> products) {
		this(null, name2, price, menuGroupId, products);
	}

	public Menu(Long id, String name, Long price, Long menuGroupId, Map<Product, Integer> products) {
		this.id = id;
		this.name = new Name(name);
		this.price = Money.valueOf(price);
		this.menuGroupId = menuGroupId;
		menuProducts.addAll(toMenuProducts(products));
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public Money getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	private List<MenuProduct> toMenuProducts(Map<Product, Integer> productsCount) {
		return productsCount.entrySet()
			.stream()
			.map(entry -> new MenuProduct(this, entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	public void validatePrice() {
		Money allProductPrices = sumAllProductsPrice();
		if (allProductPrices.isGreaterThan(price)) {
			throw new InvalidMenuPriceException(price, allProductPrices);
		}
	}

	private Money sumAllProductsPrice() {
		return menuProducts.stream()
			.map(MenuProduct::totalPrice)
			.reduce(Money.ZERO, Money::add);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Menu menu = (Menu)o;
		return id.equals(menu.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
