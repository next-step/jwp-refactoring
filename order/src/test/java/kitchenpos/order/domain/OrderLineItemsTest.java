package kitchenpos.order.domain;

import static kitchenpos.generator.OrderLineItemMenuGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.order.domain.OrderLineItem;
import kitchenpos.order.order.domain.OrderLineItems;
import kitchenpos.order.order.domain.Quantity;

@DisplayName("주문 항목들 테스트")
class OrderLineItemsTest {

	@Test
	@DisplayName("주문 항목들 생성")
	void createOrderLineItemsTest() {
		assertThatNoException()
			.isThrownBy(() -> OrderLineItems.fromSingle(
				OrderLineItem.of(후라이드_세트(), Quantity.from(1L))
			));
	}

	@Test
	@DisplayName("주문 항목들 생성 - 주문 항목이 null일 경우 예외 발생")
	void createOrderLineItemsWithNullTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderLineItems.from(null))
			.withMessage("주문 항목들은 필수입니다.");
	}

	@Test
	@DisplayName("주문 항목들 생성 - 주문 항목 리스트에 null이 포함되면 예외 발생")
	void createOrderLineItemsWithNullElementTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderLineItems.from(
				Arrays.asList(
					OrderLineItem.of(후라이드_세트(), Quantity.from(1L)),
					null
				)
			))
			.withMessage("주문 항목 리스트에 null이 포함될 수 없습니다.");
	}

}
