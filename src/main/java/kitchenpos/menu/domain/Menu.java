package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.menuGroup.domain.MenuGroup;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Embedded
	private Price price;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
	@Column(nullable = false)
	private MenuGroup menuGroup;

	@OneToMany(mappedBy = "menu_product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();


	public Menu() {
	}

	public Menu(String name, Price price, MenuGroup menuGroup) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void addMenuProducts(List<MenuProduct> menuProducts) {
		BigDecimal sum = BigDecimal.ZERO;
		for (MenuProduct menuProduct : menuProducts) {
			sum = sum.add(menuProduct.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
		}

		if (price.getPrice().compareTo(sum) > 0) {
			throw new IllegalArgumentException();
		}

		menuProducts.addAll(menuProducts);
	}
}
