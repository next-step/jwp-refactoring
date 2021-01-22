package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql"})
class OrderRestControllerTest {

	@Autowired
	OrderRestController orderRestController;

	@Autowired
	TableRestController tableRestController;

	@Test
	void create() {
		// given
		Order 주문 = 주문_객체_생성();

		// when
		Order createdOrder = orderRestController.create(주문).getBody();

		// then
		assertAll(
			() -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
			() -> assertThat(createdOrder.getOrderTableId()).isEqualTo(1L),
			() -> assertThat(createdOrder.getOrderLineItems()).map(OrderLineItem::getMenuId).contains(1L, 2L)
		);
	}

	@Test
	void list() {
		// given
		orderRestController.create(주문_객체_생성());

		// when
		List<Order> orderList = orderRestController.list().getBody();

		// then
		assertThat(orderList).hasSize(1);
		assertThat(orderList.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(orderList.get(0).getOrderTableId()).isEqualTo(1L);
		assertThat(orderList.get(0).getOrderLineItems()).map(OrderLineItem::getMenuId).contains(1L, 2L);
	}

	@Test
	void changeOrderStatus() {
		// given
		Long 주문_ID = orderRestController.create(주문_객체_생성()).getBody().getId();

		// when
		Order temp = new Order();
		temp.setOrderStatus(OrderStatus.COMPLETION.name());
		Order updatedOrder = orderRestController.changeOrderStatus(주문_ID, temp).getBody();

		// then
		assertAll(
			() -> assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
			() -> assertThat(updatedOrder.getOrderTableId()).isEqualTo(1L),
			() -> assertThat(updatedOrder.getOrderLineItems()).map(OrderLineItem::getMenuId).contains(1L, 2L)
		);
	}

	private Order 주문_객체_생성() {
		OrderLineItem 주문_항목1 = new OrderLineItem();
		주문_항목1.setMenuId(1L);
		주문_항목1.setQuantity(1);
		OrderLineItem 주문_항목2 = new OrderLineItem();
		주문_항목2.setMenuId(2L);
		주문_항목2.setQuantity(2);
		Order 주문 = new Order();
		주문.setOrderLineItems(Arrays.asList(주문_항목1, 주문_항목2));
		주문.setOrderTableId(1L);

		OrderTable temp = new OrderTable();
		temp.setEmpty(false);
		tableRestController.changeEmpty(1L, temp);

		return 주문;
	}
}
