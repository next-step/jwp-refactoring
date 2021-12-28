package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.exception.OrderException;

@DisplayName("주문 상품 일급 콜렉션 : 단위 테스트")
class OrderLineItemsTest {

	@DisplayName("주문 상품 리스트가 비어있을 경우 예외처리 테스트")
	@Test
	void creteOrderLineItemsEmptyList() {
		// given // when // then
		assertThatThrownBy(() -> {
			OrderLineItems.of(Collections.emptyList());
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
	}
}
