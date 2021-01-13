package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import kitchenpos.MySpringBootTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySpringBootTest
class OrderServiceTest {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private OrderTableDao orderTableDao;

	@DisplayName("주문을 등록한다.")
	@Test
	void create() {
		OrderLineItem orderLineItem = new OrderLineItem(
			  menuDao.findById(1L).get().getId(),
			  1
		);

		OrderTable nonEmptyOrderTable = orderTableDao.findById(11L).get();
		Order order = new Order(
			  nonEmptyOrderTable.getId(),
			  Arrays.asList(orderLineItem)
		);

		//when
		Order savedOrder = orderService.create(order);
		//then
		assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(savedOrder.getOrderedTime()).isNotNull();
		assertThat(orderService.list()).contains(savedOrder);
	}

	@DisplayName("주문항목이 없는경우 주문을 등록할 수 없다.")
	@Test
	void createWithEmptyOrderLineItems() {
		OrderTable nonEmptyOrderTable = orderTableDao.findById(11L).get();
		Order order = new Order(
			  nonEmptyOrderTable.getId(), null
		);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderService.create(order))
			  .withMessage("주문 항목 정보가 없습니다.");
	}

	@DisplayName("등록되지 않은 메뉴는 주문할 수 없다.")
	@Test
	void createWithNotExistMenu() {
		OrderLineItem orderLineItem = new OrderLineItem(0L, 1);
		Order order = new Order(
			  null, Arrays.asList(orderLineItem)
		);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderService.create(order))
			  .withMessage("주문하신 메뉴를 찾을 수 없습니다.");
	}

	@DisplayName("빈 테이블은 주문할 수 없다.")
	@Test
	void createWithEmptyTable() {
		OrderLineItem orderLineItem = new OrderLineItem(
			  menuDao.findById(1L).get().getId(),
			  1
		);

		OrderTable emptyTable = orderTableDao.findById(1L).get();
		Order order = new Order(
			  emptyTable.getId(), Arrays.asList(orderLineItem)
		);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderService.create(order))
			  .withMessage("테이블이 비어있습니다.");
	}

	@DisplayName("주문상태를 변경한다.")
	@Test
	void changeOrderStatus() {
		Order order = orderDao.findById(1L).get();

		//when
		order.changeOrderStatus(OrderStatus.COMPLETION.name());
		Order changedOrder = orderService.changeOrderStatus(order.getId(), order);

		//then
		assertThat(changedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
	}

	@DisplayName("결제 완료된 주문은 상태를 변경할 수 없다.")
	@Test
	void changeCompletion() {
		Order order = orderDao.findById(2L).get();

		//when
		order.changeOrderStatus(OrderStatus.MEAL.name());

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
			  .withMessage("결제 완료된 주문은 상태를 변경할 수 없습니다.");
	}
}
