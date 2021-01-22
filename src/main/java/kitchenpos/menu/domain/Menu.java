package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.BaseEntity;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Price;

@Entity
public class Menu extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	protected Menu() {
	}

	private Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	public static Menu of(final String name, final Price price, final MenuGroup menuGroup) {
		return new Menu(null, name, price, menuGroup);
	}

	public static Menu of(MenuRequest request, MenuGroup menuGroup) {
		return of(request.getName(), Price.of(request.getPrice()), menuGroup);
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
}
