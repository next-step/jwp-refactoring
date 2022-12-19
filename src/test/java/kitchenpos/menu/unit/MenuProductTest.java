package kitchenpos.menu.unit;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 상품 관련 단위 테스트")
public class MenuProductTest {
	@DisplayName("메뉴 상품의 가격을 계산한다.")
	@Test
	void getTotalPrice() {
		// given
		Product 햄버거 = Product.of("햄버거", BigDecimal.valueOf(5000));
		ReflectionTestUtils.setField(햄버거, "id", 1L);
		// when
		MenuProduct 햄버거_상품 = MenuProduct.of(햄버거.getId(), 2);
		// then
		assertThat(햄버거_상품.getTotalPrice(햄버거.getPrice())).isEqualTo(Price.of(BigDecimal.valueOf(5000 * 2)));
	}
}
