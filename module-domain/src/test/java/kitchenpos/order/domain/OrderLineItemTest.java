package kitchenpos.order.domain;

import static kitchenpos.menu.MenuFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

@DisplayName("주문 항목")
class OrderLineItemTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		Menu menu = 후라이드후라이드_메뉴();
		Quantity quantity = Quantity.from(1L);

		// when
		OrderLineItem orderLineItem = OrderLineItem.of(menu.getId(), quantity);

		// then
		assertThat(orderLineItem.getMenuId()).isEqualTo(menu.getId());
		assertThat(orderLineItem.getQuantity()).isEqualTo(quantity);
	}
}
