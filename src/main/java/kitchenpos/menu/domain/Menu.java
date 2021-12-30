package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeOverride;
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
import javax.persistence.Table;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Entity
@Table(name = "menu")
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
	private Name name;

	@Embedded
	@AttributeOverride(name = "price", column = @Column(name = "price", nullable = false))
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
	private MenuGroup menuGroup;

	@Embedded
	private final MenuProducts menuProducts = MenuProducts.empty();

	protected Menu() {
	}

	private Menu(Long id, Name name, Price price, MenuGroup menuGroup) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	public static Menu of(Long id, Name name, Price price, MenuGroup menuGroup) {
		validate(name, price, menuGroup);
		return new Menu(id, name, price, menuGroup);
	}

	public static Menu create(String name, BigDecimal price, MenuGroup menuGroup) {
		return of(null, Name.of(name), Price.of(price), menuGroup);
	}

	public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
		return of(id, Name.of(name), Price.of(price), menuGroup);
	}

	public static Menu of(String name, int price, MenuGroup menuGroup) {
		return of(null, Name.of(name), Price.valueOf(price), menuGroup);
	}

	private static void validate(Name name, Price price, MenuGroup menuGroup) {
		if (Objects.isNull(name)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "메뉴 이름 입력이 필요합니다");
		}
		if (Objects.isNull(price)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "메뉴 가격 입력이 필요합니다");
		}
		if (Objects.isNull(menuGroup)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "메뉴 그룹 입력이 필요합니다");
		}
	}

	public void addMenuProducts(List<MenuProduct> menuProductList) {
		menuProductList.forEach(this::addMenuProduct);
	}

	private void addMenuProduct(MenuProduct menuProduct) {
		menuProduct.setMenu(this);
		menuProducts.add(menuProduct);
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Menu menu = (Menu)o;

		return id.equals(menu.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
