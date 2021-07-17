package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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

import kitchenpos.order.application.OrderService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderRequest;
import kitchenpos.domain.OrderLineItemRequest;
import kitchenpos.domain.OrderTableRequest;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	private OrderLineItemRequest orderLineItem;
	private OrderRequest cookingOrder;

	@Mock
	private MenuRepository menuDao;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		orderLineItem = new OrderLineItemRequest(1L, 1L, 2);
		cookingOrder = new OrderRequest(1L, "COOKING", LocalDateTime.now(), Arrays.asList(orderLineItem));
	}

	@DisplayName("주문을 등록한다.")
	@Test
	void createTestInHappyCase() {
		// given
		when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(1L);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTableRequest()));
		when(orderDao.save(any())).thenReturn(cookingOrder);
		when(orderLineItemDao.save(any())).thenReturn(orderLineItem);
		// when
		OrderRequest order = orderService.create(cookingOrder);
		// then
		assertThat(order.getOrderLineItems().size()).isEqualTo(1);
		assertThat(order.getOrderStatus()).isEqualTo("COOKING");
	}

	@DisplayName("주문 항목 없이 주문을 등록하지 못한다.")
	@Test
	void createTestWithNoOrderLineItem() {
		// given
		lenient().when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(1L);
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTableRequest()));
		lenient().when(orderDao.save(any())).thenReturn(cookingOrder);
		lenient().when(orderLineItemDao.save(any())).thenReturn(orderLineItem);
		// when, then
		assertThatThrownBy(() -> orderService.create(new OrderRequest(1L, "COOKING", LocalDateTime.now(), null)));
	}

	@DisplayName("등록하지 않은 메뉴를 주문할 수 없다.")
	@Test
	void createTestWithNotExistsMenu() {
		// given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTableRequest()));
		lenient().when(orderDao.save(any())).thenReturn(cookingOrder);
		lenient().when(orderLineItemDao.save(any())).thenReturn(orderLineItem);
		// when, then
		assertThatThrownBy(() -> orderService.create(new OrderRequest(1L, "COOKING", LocalDateTime.now(), Arrays.asList(orderLineItem))));
	}

	@DisplayName("주문 테이블은 반드시 포함되어야 한다.")
	@Test
	void createTestWithNotExistsOrderTable() {
		// given
		lenient().when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(1L);
		lenient().when(orderDao.save(any())).thenReturn(cookingOrder);
		lenient().when(orderLineItemDao.save(any())).thenReturn(orderLineItem);
		// when, then
		assertThatThrownBy(() -> orderService.create(new OrderRequest(1L, "COOKING", LocalDateTime.now(), Arrays.asList(orderLineItem))));
	}

	@DisplayName("주문을 조회한다.")
	@Test
	void listTestInHappyCase() {
		// given
		when(orderDao.findAll()).thenReturn(Arrays.asList(cookingOrder));
		when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem));
		// when
		List<OrderRequest> orders = orderService.list();
		// then
		assertThat(orders.get(0).getOrderLineItems().size()).isEqualTo(1);
		assertThat(orders.get(0).getOrderStatus()).isEqualTo("COOKING");
	}

	@DisplayName("주문상태를 변경한다.")
	@Test
	void changeOrderStatusTestInHappyCase() {
		// given
		when(orderDao.findById(1L)).thenReturn(Optional.of(cookingOrder));
		// when
		OrderRequest order = orderService.changeOrderStatus(1L, new OrderRequest(1L, "COMPLETION", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest())));
		// then
		assertThat(order.getOrderStatus()).isEqualTo("COMPLETION");
	}

	@DisplayName("없는 주문의 상태는 변경하지 못한다.")
	@Test
	void changeOrderStatusTestWithNotExistOrder() {
		// given
		// when, then
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderRequest(1L, "COMPLETION", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest()))))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("완료 주문 상태를 변경하지 못한다.")
	@Test
	void changeOrderStatusTestWithCompletedStatus() {
		// given
		when(orderDao.findById(1L)).thenReturn(Optional.of(new OrderRequest(1L, "COMPLETION", LocalDateTime.now(), Arrays.asList(orderLineItem))));
		// when, then
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderRequest(1L, "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest()))))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태를 존재하지 않는 상태로 변경하지 못한다.")
	@Test
	void changeOrderStatusTestWithNotExistsStatus() {
		// given
		when(orderDao.findById(1L)).thenReturn(Optional.of(new OrderRequest(1L, "COOKING", LocalDateTime.now(), Arrays.asList(orderLineItem))));
		// when, then
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderRequest(1L, "PREPARING", LocalDateTime.now(), Arrays.asList(new OrderLineItemRequest()))))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
