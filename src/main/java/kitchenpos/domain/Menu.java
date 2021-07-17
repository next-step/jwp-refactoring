package kitchenpos.domain;

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
	private Long menuGroupId;

	@OneToMany(mappedBy = "id")
	private List<MenuProduct> menuProducts;

	public Menu() {
	}

	public Menu(Long id) {
		this.id = id;
	}

	public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(final Long menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void setMenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Menu menu = (Menu) o;
		return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId) && Objects.equals(menuProducts, menu.menuProducts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price, menuGroupId, menuProducts);
	}
}
