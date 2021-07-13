package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.domain.Product;

@DisplayName("메뉴 상품 도메인 테스트")
public class MenuProductTest {

	@DisplayName("메뉴 상품 생성 테스트")
	@Test
	void create() {
		Product product = new Product(1L, "상품", new Price(new BigDecimal(1000)));
		MenuProduct menuProduct = new MenuProduct(1L, product, new Quantity(10));
		assertThat(menuProduct).isNotNull();
	}

	@DisplayName("메뉴 상품 가격 조회 테스트")
	@Test
	void getMenuProductPrice() {
		Product product = new Product(1L, "상품", new Price(new BigDecimal(1000)));
		MenuProduct menuProduct = new MenuProduct(1L, product, new Quantity(10));
		assertThat(menuProduct.getMenuProductPrice().value()).isEqualTo(BigDecimal.valueOf(10000));
	}
}
