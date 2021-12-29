package kitchenpos.orders.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;

class OrderLineItemsTest {

	@Test
	@DisplayName("주문상품 생성")
	public void OrderLineItemsTest() {
		//given
		//when
		OrderLineItems orderLineItems = new OrderLineItems(
			Lists.newArrayList(new OrderLineItem(1L, 1L, 1L, Quantity.valueOf(2L))));
		//then
		assertThat(orderLineItems.value()).hasSize(1);
	}

	@Test
	@DisplayName("주문상품 비어있는지 검증 테스트")
	public void isEmptyOrderLineItems() {
		//given
		//when
		OrderLineItems orderLineItems = new OrderLineItems();
		//then
		assertThat(orderLineItems.isEmptyOrderLineItems()).isTrue();
	}

	@Test
	@DisplayName("주문상품에 주문번호 매칭 테스트")
	public void setOrder() {
		//given
		//when
		OrderLineItems orderLineItems = new OrderLineItems(
			Lists.newArrayList(new OrderLineItem(1L, null, 1L, Quantity.valueOf(2L))));
		//then
		OrderLineItems update = orderLineItems.setOrder(new Order(1L, 1L, OrderStatus.COOKING));
		assertThat(update.value().get(0).getOrderId()).isEqualTo(1L);
	}

}
