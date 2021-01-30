package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "menu_id")
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	public MenuProducts(List<MenuProduct> menuProducts) {
		if(menuProducts == null || menuProducts.isEmpty()) {
			throw new IllegalArgumentException("메뉴에 속한 상품들은 1개 이상이어야 합니다.");
		}
		this.menuProducts = menuProducts;
	}

	public List<MenuProduct> getList() {
		return menuProducts;
	}
}
