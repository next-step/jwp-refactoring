package kitchenpos.orders.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

	@Test
	@DisplayName("주문아이템 생성 테스트")
	public void createOrderLineItemTest() {
		//when
		OrderLineItem orderLineItem = new OrderLineItem();

		//then
		assertThat(orderLineItem).isNotNull();
	}
}
