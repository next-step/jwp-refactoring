package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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

	@DisplayName("주문 생성.")
	@Test
	void 주문_생성() {
		Long orderId = 1L;

		OrderLineItem orderLineItem = new OrderLineItem(1L, orderId, 1L, 1L);
		List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
		Order order = new Order(orderId, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(new OrderTable(1L, 1L, 3, false)));
		given(orderDao.save(order)).willReturn(order);
		given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

		assertThat(orderService.create(order)).isEqualTo(order);
	}

	@DisplayName("주문 생성. > 주문 항목이 비어있으면 안됨.")
	@Test
	void 주문_생성_주문_항목이_비어있으면_안됨() {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("주문 생성. > 주문 항목 갯수와 메뉴 갯수가 다르면 안됨.")
	@Test
	void 주문_생성_주문_항목_갯수와_메뉴_갯수가_다르면_안됨() {
		Long orderId = 1L;

		List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, orderId, 1L, 1L));
		Order order = new Order(orderId, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size() - 1);

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("주문 생성. > 주문 테이블이 없으면 안됨.")
	@Test
	void 주문_생성_주문_테이블이_없으면_안됨() {
		Long orderId = 1L;

		List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, orderId, 1L, 1L));
		Order order = new Order(orderId, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("주문 생성. > 주문 테이블이 비어있으면 안됨.")
	@Test
	void 주문_생성_주문_테이블이_비어있으면_안됨() {
		Long orderId = 1L;

		List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, orderId, 1L, 1L));
		Order order = new Order(orderId, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(new OrderTable(1L, 1L, 0, true)));

		Assertions.assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("주문 상태를 변경.")
	@Test
	void 주문_상태를_변경() {
		Long orderId = 1L;

		OrderLineItem orderLineItem = new OrderLineItem(1L, orderId, 1L, 1L);
		List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
		Order order = new Order(orderId, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		given(orderDao.findById(orderId)).willReturn(Optional.of(order));
		given(orderDao.findById(orderId)).willReturn(Optional.of(order));
		given(orderLineItemDao.findAllByOrderId(orderId)).willReturn(orderLineItems);

		assertThat(orderService.changeOrderStatus(1L, order)).isEqualTo(order);
	}

	@DisplayName("주문 상태를 변경. > 주문이 없으면 안됨.")
	@Test
	void 주문_상태를_변경_주문이_없으면_안됨() {
		Long orderId = 1L;

		OrderLineItem orderLineItem = new OrderLineItem(1L, orderId, 1L, 1L);
		List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
		Order order = new Order(orderId, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		given(orderDao.findById(orderId)).willReturn(Optional.empty());

		assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(1L, order));
	}


	@DisplayName("주문 상태를 변경. > 주문상태가 완성이면 안됨.")
	@Test
	void 주문_상태를_변경_주문상태가_완성이면_안됨() {
		Long orderId = 1L;

		OrderLineItem orderLineItem = new OrderLineItem(1L, orderId, 1L, 1L);
		List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
		Order order = new Order(orderId, 1L, OrderStatus.COMPLETION.toString(), LocalDateTime.now(), orderLineItems);

		given(orderDao.findById(orderId)).willReturn(Optional.of(order));

		assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(1L, order));
	}
}
