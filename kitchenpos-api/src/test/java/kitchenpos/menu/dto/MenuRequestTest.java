package kitchenpos.menu.dto;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class MenuRequestTest {

	@DisplayName("메뉴의 가격과 메뉴 상품들의 총 가격이 맞지 않는 경우 메뉴를 등록할 수 없음")
	@Test
	void createWithUnMatchPrice() {
		MenuRequest menuRequest = new MenuRequest("신메뉴", new BigDecimal(100), null, null);
		assertThatIllegalArgumentException()
			  .isThrownBy(() ->
					ReflectionTestUtils
						  .invokeMethod(menuRequest, "validatePrice", new BigDecimal(99))
			  )
			  .withMessage("메뉴의 가격과 메뉴 항목들의 총 가격의 합이 맞지 않습니다.");
	}
}
