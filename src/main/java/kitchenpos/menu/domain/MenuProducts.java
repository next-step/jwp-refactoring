package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.menu.exception.NotFoundMenuProductsException;

@Embeddable
public class MenuProducts {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	private MenuProducts(Menu menu, List<MenuProduct> menuProducts) {
		validate(menuProducts);
		addAll(menu, menuProducts);
	}

	private void validate(List<MenuProduct> menuProducts) {
		if (CollectionUtils.isEmpty(menuProducts)) {
			throw new NotFoundMenuProductsException();
		}
	}

	public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
		return new MenuProducts(menu, menuProducts);
	}

	private void addAll(Menu menu, List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	public List<MenuProduct> getMenuProducts() {
		return Collections.unmodifiableList(menuProducts);
	}
}
