package kitchenpos.application;

import kitchenpos.MockitoTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class OrderServiceTest extends MockitoTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private MenuDao menuDao;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@Mock
	private OrderTableDao orderTableDao;

	private OrderTable orderTable;
	private List<OrderLineItem> orderLineItems;
	private Order order;

	@BeforeEach
	void setUp() {
		orderTable = MockFixture.orderTable(5L, null, false, 10);
		orderLineItems = Arrays.asList(MockFixture.orderLineItemsForCreate(1L, 2L, 2L),
				MockFixture.orderLineItemsForCreate(2L, 3L, 1L));
		order = MockFixture.orderForCreate(orderTable.getId(), orderLineItems);

		given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
		given(menuDao.countByIdIn(any())).willReturn((long) orderLineItems.size());
		MockFixture.mockSave(orderDao, order, 1L);
	}

	@DisplayName("새로운 주문을 생성한다.")
	@Test
	void create() {
		// when
		orderService.create(order);

		// then
		Mockito.verify(orderDao).save(order);
		Mockito.verify(orderLineItemDao, times(2)).save(any());
	}

	@DisplayName("주문 생성시 인자에 메뉴가 없을 경우 예외 발생.")
	@Test
	void create_EmptyOrderLineItems() {
		given(order.getOrderLineItems()).willReturn(new ArrayList<>());

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 실제 존재하지 않는 메뉴를 인자로 했을 경우 예외 발생.")
	@Test
	void create_NotExistOrderLineItems() {
		given(menuDao.countByIdIn(anyList())).willReturn(0L);

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 실제 존재하지 않는 테이블을 인자로 했을 경우 예외 발생.")
	@Test
	void create_NotExistOrderTable() {
		given(orderTableDao.findById(any())).willReturn(Optional.empty());

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 테이블이 비어있을 경우 예외 발생.")
	@Test
	void create_EmptyOrderTable() {
		given(orderTable.isEmpty()).willReturn(true);

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("모든 주문을 조회한다.")
	@Test
	void list() {
		given(orderDao.findAll()).willReturn(Arrays.asList(order, order, order));

		assertThat(orderService.list()).hasSize(3);
	}

	@DisplayName("주문의 상태를 바꾼다.")
	@Test
	void changeOrderStatus() {
		// given
		given(order.getOrderStatus()).willReturn(OrderStatus.COOKING.name());
		given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
		Order orderStatus = mock(Order.class);
		given(orderStatus.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());

		// when
		orderService.changeOrderStatus(order.getId(), orderStatus);

		// then
		verify(orderDao).save(order);
	}

	@DisplayName("주문 상태 변경시 이미 완료된 주문을 바꿀시 예외 발생.")
	@Test
	void changeOrderStatus_StatusWrong() {
		// given
		given(order.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());
		given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
		Order orderStatus = mock(Order.class);
		given(orderStatus.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());

		// when then
		assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
