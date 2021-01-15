package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.menugroup.domain.MenuGroup;

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

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	public Menu() {
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
			throw new IllegalArgumentException("메뉴의 가격은 0원 이상이어야 합니다.");
		}

		BigDecimal sumOfMenuProductPrice = sumOfMenuProductPrice(menuProducts);
		if (price.compareTo(sumOfMenuProductPrice) != 0) {
			throw new IllegalArgumentException("메뉴의 가격과 메뉴 항목들의 총 가격의 합이 맞지 않습니다.");
		}
	}

	private BigDecimal sumOfMenuProductPrice(List<MenuProduct> menuProducts) {
		return menuProducts.stream()
			  .map(menuProduct -> menuProduct.sumOfPrice())
			  .reduce((p1, p2) -> p1.add(p2))
			  .orElseThrow(IllegalArgumentException::new);
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
