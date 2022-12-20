package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Embedded
	private Price price;

	@ManyToOne(optional = false)
	@JoinColumn(name = "menu_group_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public static Menu of(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		return new Menu(name, price, menuGroup, menuProducts);
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
		return price.getValue();
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}
}
