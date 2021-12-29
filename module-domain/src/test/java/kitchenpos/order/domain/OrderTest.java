package kitchenpos.order.domain;

import static kitchenpos.order.OrderLineItemFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.ordertable.domain.OrderTable;

@DisplayName("주문")
class OrderTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		OrderTable orderTable = 비어있지않은_주문_테이블_1번();
		List<OrderLineItemDto> orderLineItemDtos = Collections.singletonList(
			OrderLineItemDto.from(후라이드후라이드_메뉴_주문_항목()));

		// when
		Order order = Order.of(
			orderTable.getId(),
			orderLineItemDtos,
			new ValidOrderValidator());

		// then
		assertThat(order).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 테이블이 비어있는 경우")
	@Test
	void ofFailOnEmptyOrderTable() {
		// given
		OrderTable orderTable = 비어있지않은_주문_테이블_1번();
		List<OrderLineItemDto> orderLineItemDtos = Collections.singletonList(
			OrderLineItemDto.from(후라이드후라이드_메뉴_주문_항목()));

		// when
		ThrowingCallable throwingCallable = () -> Order.of(
			orderTable.getOrderTableGroupId(),
			orderLineItemDtos,
			new OrderTableEmptyOrderValidator());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
