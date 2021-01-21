package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Price;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	public Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup,
		final MenuProducts menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
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

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}
}
