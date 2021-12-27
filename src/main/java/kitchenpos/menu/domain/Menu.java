package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.common.Price;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Embedded
	@Column(length = 19, scale = 2, nullable = false)
	private Price price;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "menuGroupId", columnDefinition = "bigint(20)", nullable = false)
	private MenuGroup menuGroup;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	public Menu() {
	}

	public Menu(Long id) {
		this.id = id;
	}

	public Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		this(null, name, price, menuGroup, menuProducts);
	}

	public Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		validateMenuPriceIsNullOrLessThanZero(price);
		validateMenuPriceSumGreaterThanProductsSum(menuProducts, price);

		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = setMenu(menuProducts);
	}

	private List<MenuProduct> setMenu(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			.map(mp -> new MenuProduct(this, mp.getProduct(), mp.getQuantity()))
			.collect(Collectors.toList());
	}

	private void validateMenuPriceSumGreaterThanProductsSum(List<MenuProduct> menuProducts, Price price) {
		BigDecimal sum = menuProducts.stream()
			.map(mp -> mp.getProductPrice().multiply(BigDecimal.valueOf(mp.getQuantity())))
			.reduce(BigDecimal::add)
			.orElseThrow(IllegalArgumentException::new);
		if (price.greaterThan(sum)) {
			throw new IllegalArgumentException("메뉴의 가격은 상품들의 가격합보다 작거나 같아야 합니다");
		}
	}

	private void validateMenuPriceIsNullOrLessThanZero(Price price) {
		if (Objects.isNull(price) || price.lessThanZero()) {
			throw new IllegalArgumentException("메뉴의 가격은 0보다 작을 수 없습니다");
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price.value();
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public long getMenuGroupId() {
		return this.menuGroup.getId();
	}
}
