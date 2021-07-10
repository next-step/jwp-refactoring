package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.menu.exception.MenuException;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menuGroupId", foreignKey = @ForeignKey(name = "fk_menu_group_to_menu"), nullable = false)
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {

	}

	public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validate(price, menuGroup, menuProducts);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = menuProducts;
	}

	public Menu(long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		this(name, price, menuGroup, menuProducts);
		this.id = id;
	}

	private void validate(Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		validateMenuGroup(menuGroup);
		validateMenuProducts(menuProducts);
        validatePrice(price, menuProducts);
	}

	private void validateMenuGroup(MenuGroup menuGroup) {
		if (Objects.isNull(menuGroup)) {
			throw new MenuException("메뉴 그룹이 비워져 있습니다.");
		}
	}

	private void validateMenuProducts(MenuProducts menuProducts) {
		if (Objects.isNull(menuProducts)) {
			throw new MenuException("메뉴 상품들이 하나도 존재하지 않습니다.");
		}
	}

	private void validatePrice(Price price, MenuProducts menuProducts) {
		if (price.compareTo(menuProducts.getSumMenuProductPrice()) > 0) {
			throw new MenuException("메뉴의 가격이 메뉴 상품들의 총합보다 클 수 없습니다.");
		}
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
