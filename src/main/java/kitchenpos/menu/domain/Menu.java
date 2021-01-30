package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Price;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected Menu() {
	}

	public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
		this.name = name;
		this.price = new Price(price);
		this.menuGroup = menuGroup;
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
		return menuProducts;
	}

	public void addAllMenuProduct(final List<MenuProduct> menuProducts) {
		validatePriceEquals(this.getPrice(), menuProducts);
		this.menuProducts.addAll(menuProducts);
	}

	private void validatePriceEquals(final BigDecimal price, final List<MenuProduct> menuProducts) {
		BigDecimal sum = BigDecimal.valueOf(menuProducts.stream()
			.map(MenuProduct::getPrice)
			.mapToLong(BigDecimal::longValue)
			.sum());
		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException("메뉴 상품의 가격(상품 가격 * 수량)의 합이 메뉴의 가격보다 작습니다.");
		}
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
