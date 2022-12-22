package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "menu_id")
	private final List<MenuProduct> menuProducts = new ArrayList<>();

	protected Menu() {
	}

	public Menu(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
		this.name = new Name(name);
		this.price = Money.valueOf(price);
		this.menuGroupId = menuGroupId;
		this.menuProducts.addAll(menuProducts);
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
