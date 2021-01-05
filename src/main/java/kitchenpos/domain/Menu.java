package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.exception.WrongPriceException;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

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
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < products.size(); i++) {
			this.menuProducts.add(MenuProduct.create(this, products.get(i), quantities.get(i)));
			sum = sum.add(products.get(i).getPrice().multiply(BigDecimal.valueOf(quantities.get(i))));
		}
		validatePrice(price, sum);
	}

	private void validatePrice(BigDecimal price, BigDecimal sum) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new WrongPriceException("메뉴의 가격이 없거나 0보다 작습니다.");
		}
		if (price.compareTo(sum) > 0) {
			throw new WrongPriceException("메뉴의 가격이 상품가격의 총합보다 클 수 없습니다.");
		}
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
		return menuProducts;
	}
}
