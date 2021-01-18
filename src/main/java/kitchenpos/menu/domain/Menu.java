package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
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
	private MenuGroup menuGroup;//메뉴의 대분류

	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	public Menu() {
	}

	public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
		validate(price);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
		menuProducts.setMenu(this);
		menuProducts.validate(price);
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

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}

	public void setMenuProducts(final MenuProducts menuProducts) {
		this.menuProducts = menuProducts;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}
}
