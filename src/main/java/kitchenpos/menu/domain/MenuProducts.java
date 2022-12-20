package kitchenpos.menu.domain;

import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu")
	private List<MenuProduct> menuProducts;

	protected MenuProducts() {
	}

	private MenuProducts(List<MenuProduct> menuProducts) {
		Assert.notNull(menuProducts, "메뉴 상품 리스트는 필수입니다.");
		Assert.noNullElements(menuProducts,
			() -> String.format("메뉴 상품 리스트(%s)에 null이 포함될 수 없습니다.", menuProducts));
		this.menuProducts = menuProducts;
	}

	public static MenuProducts from(List<MenuProduct> menuProducts) {
		return new MenuProducts(menuProducts);
	}
	public static MenuProducts fromSingle(MenuProduct menuProduct) {
		return new MenuProducts(Collections.singletonList(menuProduct));
	}

	public List<MenuProduct> list() {
		return Collections.unmodifiableList(menuProducts);
	}

	public Price totalPrice() {
		return menuProducts.stream()
			.map(MenuProduct::price)
			.reduce(Price::sum)
			.orElse(Price.ZERO);
	}

	@Override
	public String toString() {
		return "MenuProducts{" +
			"menuProducts=" + menuProducts +
			'}';
	}

	public void updateMenu(Menu menu) {
		menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
	}
}
