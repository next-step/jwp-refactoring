package kitchenpos.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

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

	private final Product 초밥 = product(1L, "초밥", 3_000);
	private final MenuProduct 메뉴초밥 = menuProduct(1L, 초밥.getId(), 10);
	private final MenuGroup 메인메뉴그룹 = menuGroup(1L, "메인");
	private final Menu 일식메뉴 = menu(1L, "일식", 30_000, 메인메뉴그룹.getId(), Arrays.asList(메뉴초밥));
	final OrderTable 개별_주문테이블 = orderTable(1L, null, 4, false);

	@Test
	void create() {
		final OrderLineItem 주문항목 = orderLineItem(1L, 1L, 일식메뉴.getId(), 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), 주문항목목록);

		given(menuDao.countByIdIn(any())).willReturn(1L);
		given(orderTableDao.findById(any())).willReturn(Optional.of(개별_주문테이블));
		given(orderDao.save(any())).willReturn(주문);
		given(orderLineItemDao.save(any())).willReturn(주문항목);

		final Order createdOrder = orderService.create(
			order(개별_주문테이블.getId(), Arrays.asList(orderLineItem(일식메뉴.getId(), 2)))
		);

		assertThat(createdOrder.getId()).isNotNull();
		assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(createdOrder.getOrderedTime()).isNotNull();
		assertThat(createdOrder.getOrderLineItems().size()).isEqualTo(1);
		assertThat(createdOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
	}

	@Test
	void create_empty_order_line_items() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(개별_주문테이블.getId(), Collections.emptyList())
			));
	}

	@Test
	void create_not_found_menu() {
		given(menuDao.countByIdIn(any())).willReturn(0L);

		final Long nullMenuId = null;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(개별_주문테이블.getId(), Arrays.asList(orderLineItem(nullMenuId, 2)))
			));
	}

	@Test
	void create_not_found_order_table() {
		given(menuDao.countByIdIn(any())).willReturn(1L);
		given(orderTableDao.findById(any())).willReturn(Optional.empty());

		final Long nullOrderTableId = null;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(nullOrderTableId, Arrays.asList(orderLineItem(일식메뉴.getId(), 2)))
			));
	}

	@Test
	void create_empty_order_table() {
		final OrderTable 빈_주문테이블 = orderTable(1L, null, 0, true);

		given(menuDao.countByIdIn(any())).willReturn(1L);
		given(orderTableDao.findById(any())).willReturn(Optional.of(빈_주문테이블));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(빈_주문테이블.getId(), Arrays.asList(orderLineItem(일식메뉴.getId(), 2)))
			));
	}

	@Test
	void list() {
		final OrderLineItem 주문항목 = orderLineItem(1L, 1L, 일식메뉴.getId(), 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), 주문항목목록);

		given(orderDao.findAll()).willReturn(Arrays.asList(주문));
		given(orderLineItemDao.findAllByOrderId(any())).willReturn(주문항목목록);

		final List<Order> orders = orderService.list();

		assertThat(orders).containsExactly(주문);
		assertThat(orders.get(0).getOrderLineItems()).containsExactly(주문항목);
	}

	@Test
	void changeOrderStatus() {
		final OrderLineItem 주문항목 = orderLineItem(1L, 1L, 일식메뉴.getId(), 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), 주문항목목록);

		given(orderDao.findById(any())).willReturn(Optional.of(주문));
		given(orderDao.save(any())).willReturn(주문);
		given(orderLineItemDao.findAllByOrderId(any())).willReturn(주문항목목록);

		final Order changedOrder = orderService.changeOrderStatus(주문.getId(), order(1L, OrderStatus.MEAL));

		assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	void changeOrderStatus_completion_order() {
		final OrderLineItem 주문항목 = orderLineItem(1L, 1L, 일식메뉴.getId(), 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), 주문항목목록);

		given(orderDao.findById(any())).willReturn(Optional.of(주문));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), order(1L, OrderStatus.COOKING)));
	}
}
