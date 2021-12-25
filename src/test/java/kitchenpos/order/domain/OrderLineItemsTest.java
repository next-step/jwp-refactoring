package kitchenpos.order.domain;

import static kitchenpos.menu.MenuFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import kitchenpos.common.domain.Quantity;

@DisplayName("주문 항목들")
class OrderLineItemsTest {
	@DisplayName("생성")
	@Test
	void from() {
		// given
		OrderLineItem orderLineItem = OrderLineItem.of(
			후라이드후라이드_메뉴(),
			Quantity.from(1L));

		// when
		OrderLineItems orderLineItems = OrderLineItems.from(Collections.singletonList(orderLineItem));

		// then
		assertThat(orderLineItems).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 항목이 없는 경우")
	@ParameterizedTest
	@NullAndEmptySource
	void fromFailOnEmptyOrderLineItem(List<OrderLineItem> orderLineItems) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> OrderLineItems.from(orderLineItems);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
