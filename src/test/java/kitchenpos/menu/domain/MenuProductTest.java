package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

@DisplayName("단위 테스트 : 메뉴 상품 관")
public class MenuProductTest {

	private Product product;
	private MenuProduct menuProduct;

	@BeforeEach
	void setup() {
		product = Product.of("p1", 10000);
		menuProduct = MenuProduct.of(null, product, 1L);
	}

	@DisplayName("메뉴 상품 갯수와 상품 가격을 곱한 최종 가격을 반환하는 메소드 테스트")
	@Test
	void calculateTotalPrice() {
		// when
		Price totalPrice = menuProduct.getTotalPrice();

		// then
		assertThat(totalPrice.getPrice().intValue()).isEqualTo(10000);
	}
}
