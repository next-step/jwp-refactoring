package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.menu.exception.InvalidMenuException;

@Embeddable
public class MenuProducts {

	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	private MenuProducts(Menu menu, List<MenuProduct> menuProducts) {
		validate(menuProducts);
		addAll(menu, menuProducts);
	}

	private void validate(List<MenuProduct> menuProducts) {
		if (CollectionUtils.isEmpty(menuProducts)) {
			throw new InvalidMenuException("메뉴를 구성하는 상품 목록이 있어야 합니다.");
		}
	}

	public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
		return new MenuProducts(menu, menuProducts);
	}

	private void addAll(Menu menu, List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
		this.menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
	}

	public List<MenuProduct> getMenuProducts() {
		return Collections.unmodifiableList(menuProducts);
	}
}
