package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu extends BaseIdEntity {

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "price", nullable = false, columnDefinition = "DECIMAL(19,2)")
	private BigDecimal price;

	@ManyToOne
	@JoinColumn(name = "menu_group_id", nullable = false)
	private MenuGroup menuGroup;

	@OneToMany(mappedBy = "menu") // TODO: 2021-01-16 consider cascade, orphanRemoval
	private List<MenuProduct> menuProducts;

	protected Menu() {
	}

	public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = new ArrayList<>();
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

	public void addMenuProducts(MenuProduct menuProduct) {
		this.menuProducts.add(menuProduct);
	}

	public void addMenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}
}
