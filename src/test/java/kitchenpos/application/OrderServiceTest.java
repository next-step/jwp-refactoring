package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 기능 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	private Order order;
	private OrderLineItem orderLineItem;
	private List<OrderLineItem> orderLineItems;
	private Menu menu;
	private OrderTable orderTable;

	@Mock
	private MenuDao menuDao;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	void setup() {
		orderLineItems = new ArrayList<>();

		orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		orderLineItems.add(orderLineItem);

		orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setTableGroupId(1L);
		orderTable.setNumberOfGuests(0);

		order = new Order();
		order.setId(1L);
		order.setOrderLineItems(orderLineItems);
		order.setOrderStatus(String.valueOf(OrderStatus.COOKING));
		order.setOrderTableId(orderTable.getId());
	}

	@DisplayName("주문을 생성할 수 있다.")
	@Test
	public void create() {
		// given
		given(menuDao.countByIdIn(any())).willReturn(1L);
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderLineItemDao.save(any())).willReturn(orderLineItem);
		given(orderDao.save(any())).willReturn(order);

		// when
		Order createdOrder = orderService.create(order);

		// then
		assertThat(createdOrder.getId()).isEqualTo(this.order.getId());
	}

	@DisplayName("주문 테이블이 존재하지 않으면 생성할 수 없다.")
	@Test
	public void createNoMenuTable() {
		// given
		given(menuDao.countByIdIn(any())).willReturn(1L);
		order.setOrderTableId(null);

		// when
		assertThrows(IllegalArgumentException.class, () -> {
			orderService.create(order);
		});
	}

	@DisplayName("주문의 목록을 조회할 수 있다.")
	@Test
	public void list() {
		// given
		given(orderDao.findAll()).willReturn(Arrays.asList(order));

		// when
		List<Order> orders = orderService.list();

		// then
		assertThat(orders).containsExactly(order);
	}

	@DisplayName("주문의 상태를 변경할 수 있다.")
	@Test
	public void changeOrderStatus() {
		Order changedOrder = new Order();
		changedOrder.setOrderStatus(String.valueOf(OrderStatus.COMPLETION));

		given(orderDao.findById(any())).willReturn(Optional.of(order));
		given(orderDao.save(any())).willReturn(changedOrder);
		given(orderLineItemDao.findAllByOrderId(any())).willReturn(orderLineItems);

		Order resultOrder = orderService.changeOrderStatus(order.getId(), changedOrder);

		assertThat(resultOrder.getOrderStatus()).isEqualTo(changedOrder.getOrderStatus());
	}

	@DisplayName("주문의 상태가 계산 완료이면 주문을 변경 할 수 없다.")
	@Test
	public void OrderStatusCompletion() {
		// given
		Order newOrder = new Order();
		newOrder.setOrderStatus(String.valueOf(OrderStatus.COMPLETION));
		given(orderDao.findById(any())).willReturn(Optional.of(newOrder));

		// when
		assertThrows(IllegalArgumentException.class, () -> {
			orderService.changeOrderStatus(order.getId(), newOrder);
		});
	}

	@DisplayName("주문이 생성되면 조리 상태가 된다.")
	@Test
	public void OrderStatusCook() {
		// given
		given(menuDao.countByIdIn(any())).willReturn(1L);
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderLineItemDao.save(any())).willReturn(orderLineItem);
		given(orderDao.save(any())).willReturn(order);

		// when
		Order createdOrder = orderService.create(order);

		// then
		assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
	}
}
