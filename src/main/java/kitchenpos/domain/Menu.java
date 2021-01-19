package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.common.MenuValidationException;
import kitchenpos.common.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu extends BaseIdEntity {

	static final String MSG_PRICE_RULE = "Menu price must be equal or low than products prices' total sum";

	@Column(name = "name", nullable = false)
	private String name;

	@Embedded
	private Price price;

	@ManyToOne
	@JoinColumn(name = "menu_group_id", nullable = false)
	private MenuGroup menuGroup;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	public Menu(String name, int price, MenuGroup menuGroup) {
		this(name, new Price(new BigDecimal(price)), menuGroup);
	}

	public Menu(String name, Price price, MenuGroup menuGroup) {
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
		this.menuProducts = new MenuProducts(new ArrayList<>());
	}

	public void addMenuProducts(List<MenuProduct> menuProducts) {
		validatePrice(menuProducts);
		this.menuProducts.add(menuProducts);
	}

	private void validatePrice(List<MenuProduct> menuProducts) {
		Price beforePriceSum = this.menuProducts.getAllPrice();
		Price afterPriceSum = beforePriceSum.add(new MenuProducts(menuProducts).getAllPrice());
		if (this.price.compareTo(afterPriceSum) > 0) {
			throw new MenuValidationException(MSG_PRICE_RULE);
		}
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

	public Iterator<MenuProduct> getMenuProducts() {
		return menuProducts.iterator();
	}
}
