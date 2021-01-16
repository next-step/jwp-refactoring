package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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
	private MenuGroup menuGroup;//메뉴의 대분류

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>(); // 속한 메뉴

	protected Menu() {
	}

	public Menu(String name, BigDecimal price, MenuGroup menuGroup,
	            List<MenuProduct> menuProducts) {
		validate(price, menuProducts);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
		setMenu();
	}

	private void setMenu() {
		this.menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
	}

	private void validate(BigDecimal price, List<MenuProduct> menuProducts) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
		}

		BigDecimal sum = sumMenuProductsPrice(menuProducts);
		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException("메뉴의 가격과 메뉴 항목들의 총 가격의 합이 맞지 않습니다.");
		}
	}

	private BigDecimal sumMenuProductsPrice(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
				.map(menuProduct -> menuProduct.sumOfPrice())
				.reduce((p1, p2) -> p1.add(p2))
				.orElseThrow(IllegalArgumentException::new);
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

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void setMenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}
}
