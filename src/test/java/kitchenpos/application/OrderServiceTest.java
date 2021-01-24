package kitchenpos.application;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderServiceTest {
	@Autowired
	private OrderService orderService;
	@Autowired
	private TableService tableService;

	@DisplayName("주문 항목이 없으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException1() {
		Order order = createOrder(1L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("주문 항목에 없는 메뉴가 있으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException2() {
		OrderLineItem orderLineItem1 = createOrderLineItem(1L, 2L);
		OrderLineItem orderLineItem2 = createOrderLineItem(10L, 2L);

		Order order = createOrder(1L, orderLineItem1, orderLineItem2);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("등록되지 않은 테이블이면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException3() {
		OrderLineItem orderLineItem = createOrderLineItem(1L, 2L);

		Order order = createOrder(10L, orderLineItem);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("빈 테이블이면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException4() {
		OrderLineItem orderLineItem = createOrderLineItem(1L, 2L);

		Order order = createOrder(1L, orderLineItem);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("주문 성공")
	@Test
	void create() {
		Order resultOrder = 주문_접수(1L, 1L, 2L);

		assertThat(resultOrder.getId()).isNotNull();
		resultOrder.getOrderLineItems().forEach(it -> assertThat(it.getOrderId()).isNotNull());
	}

	private Order 주문_접수(Long tableId, Long menuId, long quantity) {
		OrderTable orderTable = createOrderTable(false);
		OrderTable savedOrderTable = tableService.changeEmpty(tableId, orderTable);

		OrderLineItem orderLineItem = createOrderLineItem(menuId, quantity);
		Order order = createOrder(savedOrderTable.getId(), orderLineItem);

		return orderService.create(order);
	}

	@DisplayName("주문 목록 조회")
	@Test
	void list() {
		주문_접수(1L, 1L, 2L);
		주문_접수(2L, 2L, 2L);

		List<Order> orders = orderService.list();

		assertThat(orders).hasSize(2);
		assertThat(orders.stream().map(Order::getOrderLineItems).collect(Collectors.toList())).hasSize(2);
	}

	@DisplayName("주문 번호가 없으면 IllegalArgumentException 발생")
	@Test
	void changeOrderStatus_ThrowIllegalArgumentException1() {
		OrderLineItem orderLineItem = createOrderLineItem(1L, 2L);
		Order order = createOrder(1L, orderLineItem);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.changeOrderStatus(1L, order));
	}

	@DisplayName("주문 상태 변경")
	@Test
	void changeOrderStatus() {
		Order savedOrder = 주문_접수(1L, 1L, 2L);
		Order order = createOrder(OrderStatus.COMPLETION.name());

		Order resultOrder = orderService.changeOrderStatus(savedOrder.getId(), order);

		assertThat(resultOrder.getId()).isEqualTo(savedOrder.getId());
		assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
	}

	@DisplayName("주문 상태가 완료면 IllegalArgumentException 발생")
	@Test
	void changeOrderStatus_ThrowIllegalArgumentException2() {
		Order savedOrder = 주문_접수(1L, 1L, 2L);
		Order order1 = createOrder(1L, OrderStatus.COMPLETION.name());
		Order order2 = createOrder(1L, OrderStatus.MEAL.name());

		orderService.changeOrderStatus(savedOrder.getId(), order1);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), order2));
	}
}