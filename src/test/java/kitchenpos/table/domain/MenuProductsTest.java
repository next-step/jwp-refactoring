package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

class MenuProductsTest {

	@Test
	void getTotalPrice() {

		MenuProducts menuProducts = new MenuProducts();
		Product product = new Product("123", new Price(BigDecimal.valueOf(3000)));
		MenuProduct menuProduct = new MenuProduct(product, 3L);
		menuProducts.addMenuProduct(menuProduct);

		Price actual = menuProducts.getTotalPrice();

		assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(9000)));
	}

}