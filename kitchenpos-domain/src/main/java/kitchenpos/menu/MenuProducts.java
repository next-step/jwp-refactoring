package kitchenpos.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
	@OneToMany(mappedBy = "menuId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	public MenuProducts(Long menuId, List<MenuProduct> menuProducts) {
		validate(menuProducts);
		menuProducts.forEach(menuProduct -> menuProduct.setMenuId(menuId));
		this.menuProducts = menuProducts;
	}

	private void validate(List<MenuProduct> menuProducts) {
		if (Objects.isNull(menuProducts)) {
			throw new IllegalArgumentException("메뉴 상품 정보가 없습니다.");
		}
	}

	public List<MenuProduct> getList() {
		return Collections.unmodifiableList(menuProducts);
	}

	public int sumPrices() {
		return menuProducts.stream()
			.mapToInt(MenuProduct::calculatePrice)
			.sum();
	}
}
