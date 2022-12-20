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

	// TODO remove bidirection
	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<MenuProduct> menuProducts = new ArrayList<>();

	protected Menu() {
	}

	public Menu(String name, Long price, Long menuGroupId, Map<Product, Integer> products) {
		this(null, name, Money.valueOf(price), menuGroupId, products);
	}

	public Menu(String name, Money price, Long menuGroupId, Map<Product, Integer> products) {
		this(null, name, price, menuGroupId, products);
	}

	public Menu(Long id, String name, Money price, Long menuGroupId, Map<Product, Integer> products) {
		this.id = id;
		this.name = new Name(name);
		this.price = price;
		this.menuGroupId = menuGroupId;
		menuProducts.addAll(toMenuProducts(products));
	}

	public Menu(String name, long price, Long menuGroupId, List<Product> products) {
		this.name = new Name(name);
		this.price = Money.valueOf(price);
		this.menuGroupId = menuGroupId;
		this.menuProducts.addAll(MenuProduct.of(this, products));
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

	public Money sumAllProductsPrice() {
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
