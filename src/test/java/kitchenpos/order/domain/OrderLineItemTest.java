package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;
import kitchenpos.generator.MenuGenerator;

@DisplayName("주문 항목 테스트")
class OrderLineItemTest {

	@Test
	@DisplayName("주문 항목 생성")
	void createOrderLineItemTest() {
		assertThatNoException()
			.isThrownBy(() -> OrderLineItem.of(
				MenuGenerator.후라이드_세트(),
				Quantity.from(1L)
			));
	}

	@Test
	@DisplayName("주문 항목 생성 - 메뉴가 null일 경우 예외 발생")
	void createOrderLineItemTest_menuIsNull() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderLineItem.of(
				null,
				Quantity.from(1L)
			))
			.withMessage("메뉴는 필수입니다.");
	}

	@Test
	@DisplayName("주문 항목 생성 - 수량이 null일 경우 예외 발생")
	void createOrderLineItemTest_quantityIsNull() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderLineItem.of(
				MenuGenerator.후라이드_세트(),
				null
			))
			.withMessage("수량은 필수입니다.");
	}
}
