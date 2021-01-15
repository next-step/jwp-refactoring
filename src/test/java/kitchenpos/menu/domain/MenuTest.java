package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class MenuTest {

	@DisplayName("0원 미만의 메뉴는 등록할 수 없음")
	@Test
	void createWithUnderZeroPrice() {
		assertThatIllegalArgumentException()
			  .isThrownBy(() ->
					new Menu("신메뉴", new BigDecimal(-1), new MenuGroup(),
						  Arrays.asList(new MenuProduct()))
			  )
			  .withMessage("메뉴의 가격은 0원 이상이어야 합니다.");
	}

	@DisplayName("메뉴의 가격과 메뉴 상품들의 총 가격이 맞지 않는 경우 메뉴를 등록할 수 없음")
	@Test
	void createWithUnMatchPrice() {
		Product product = new Product("상품", new BigDecimal(1));
		MenuProduct menuProduct = new MenuProduct();
		ReflectionTestUtils.setField(menuProduct, "product", product);
		ReflectionTestUtils.setField(menuProduct, "quantity", 1L);

		assertThatIllegalArgumentException()
			  .isThrownBy(() ->
					new Menu("신메뉴", new BigDecimal(0), new MenuGroup(),
						  Arrays.asList(menuProduct))
			  )
			  .withMessage("메뉴의 가격과 메뉴 항목들의 총 가격의 합이 맞지 않습니다.");
	}
}
