package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>(); // 속한 메뉴

	public MenuProducts() {
	}

	public MenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = menuProducts;

	}

	public void setMenu(Menu menu) {
		this.menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
	}

	protected BigDecimal sumMenuProductsPrice() {
		BigDecimal sum = this.menuProducts.stream()
				.map(menuProduct -> menuProduct.sumOfPrice())
				.reduce((p1, p2) -> p1.add(p2))
				.orElseThrow(IllegalArgumentException::new);
		return sum;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts;
	}

	public void validate(BigDecimal price) {
		BigDecimal sum = this.sumMenuProductsPrice();
		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException("메뉴에 속한 메뉴 상품의 총 합은 메뉴 가격보다 같거나 커야합니다.");
		}
	}
}

