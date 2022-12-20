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

import org.springframework.util.Assert;

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
		validate(name, price, menuGroup, menuProducts);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		menuProducts.updateMenu(this);
		this.menuProducts = menuProducts;
	}

	public static Menu of(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		return new Menu(name, price, menuGroup, menuProducts);
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public BigDecimal price() {
		return price.value();
	}

	public MenuGroup menuGroup() {
		return menuGroup;
	}

	public MenuProducts menuProducts() {
		return menuProducts;
	}

	private void validate(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		Assert.notNull(name, "메뉴 이름은 필수입니다.");
		Assert.notNull(price, "메뉴 가격은 필수입니다.");
		Assert.notNull(menuGroup, "메뉴 그룹은 필수입니다.");
		Assert.notNull(menuProducts, "메뉴 상품은 필수입니다.");
		Price totalPrice = menuProducts.totalPrice();
		Assert.isTrue(totalPrice.graterThanOrEqualTo(price), "메뉴 가격은 메뉴 상품의 총 가격보다 작거나 같아야 합니다.");
	}
}
