package kitchenpos.domain;

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

	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	protected Menu() {
	}

	private Menu(String name, BigDecimal price, MenuGroup menuGroup) {
		validatePrice(price);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	private void validatePrice(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new WrongPriceException("메뉴의 가격이 없거나 0보다 작습니다.");
		}
	}

	public static Menu create(String name, BigDecimal price, MenuGroup menuGroup) {
		return new Menu(name, price, menuGroup);
	}

	public void addMenuProduct(List<MenuProduct> menuProducts) {
		validatePriceSum(menuProducts);
		this.menuProducts.add(menuProducts);
	}

	private void validatePriceSum(List<MenuProduct> menuProducts) {
		BigDecimal totalPrice = menuProducts.stream()
			.map(MenuProduct::getPrice)
			.reduce(BigDecimal::add)
			.orElse(BigDecimal.ZERO);

		if (price.compareTo(totalPrice) > 0) {
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
		return menuProducts.getMenuProducts();
	}
}
