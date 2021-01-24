package kitchenpos.ui;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.table.ui.TableRestController;

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
			() -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
			() -> assertThat(createdOrder.getOrderTable().getId()).isEqualTo(주문_테이블1.getId()),
			() -> assertThat(createdOrder.getOrderLineItems())
				.map(OrderLineItem::getMenu)
				.map(Menu::getName)
				.contains(메뉴1.getName(), 메뉴2.getName())
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
		assertThat(orderList.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
		assertThat(orderList.get(0).getOrderTable().getId()).isEqualTo(주문_테이블1.getId());
		assertThat(orderList.get(0).getOrderLineItems())
			.map(OrderLineItem::getMenu)
			.map(Menu::getName)
			.contains(메뉴1.getName(), 메뉴2.getName());
	}

	@Test
	void changeOrderStatus() {
		// given
		Long 주문_ID = orderRestController.create(주문_객체_생성()).getBody().getId();

		// when
		Order temp = new Order();
		temp.setOrderStatus(OrderStatus.COMPLETION);
		Order updatedOrder = orderRestController.changeOrderStatus(주문_ID, temp).getBody();

		// then
		assertAll(
			() -> assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION),
			() -> assertThat(updatedOrder.getOrderTable().getId()).isEqualTo(주문_테이블1.getId()),
			() -> assertThat(updatedOrder.getOrderLineItems())
				.map(OrderLineItem::getMenu)
				.map(Menu::getName)
				.contains(메뉴1.getName(), 메뉴2.getName())
		);
	}

	private Order 주문_객체_생성() {
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴2).quantity(2L).build();
		Order 주문 = new Order.Builder().orderTable(주문_테이블1).orderLineItems(주문_항목1, 주문_항목2).build();

		tableRestController.changeEmpty(주문_테이블1.getId(), new OrderTable.Builder().empty(false).build());

		return 주문;
	}
}
