package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private BigDecimal price;

	@ManyToOne
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	@Embedded
	private final MenuProducts menuProducts = new MenuProducts();

	public Menu() {
	}

	public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
		return new Menu(name, price, menuGroup);
	}

	public void validatePrice() {
		validatePrice(this.price, sumOfMenuProductPrice(this.menuProducts.getMenuProducts()));
	}

	private BigDecimal sumOfMenuProductPrice(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
				.map(MenuProduct::sumOfPrice)
				.reduce((p1, p2) -> p1.add(p2))
				.orElseThrow(IllegalArgumentException::new);
	}

	public void validatePrice(BigDecimal price, BigDecimal sumOfMenuProductPrice) {
		if (price.compareTo(sumOfMenuProductPrice) != 0) {
			throw new IllegalArgumentException("메뉴의 가격과 메뉴 항목들의 총 가격의 합이 맞지 않습니다.");
		}
	}

	public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
		validate(price);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	private void validate(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("가격은 0보다 큰 숫자여야 합니다.");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public void add(MenuProduct menuProduct) {
		this.menuProducts.add(menuProduct);
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}

}
