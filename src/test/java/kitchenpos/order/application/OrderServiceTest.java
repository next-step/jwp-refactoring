package kitchenpos.order.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderLineItemRepository orderLineItemRepository;
	@Mock
	private OrderTableRepository orderTableRepository;

	private final Product 초밥 = product(1L, "초밥", 3_000);
	private final MenuProduct 메뉴초밥 = menuProduct(1L, null, 초밥, 10);
	private final MenuGroup 메인메뉴그룹 = menuGroup(1L, "메인");
	private final Menu 일식메뉴 = menu(1L, "일식", 30_000, 메인메뉴그룹, Arrays.asList(메뉴초밥));
	final OrderTable 개별_주문테이블 = orderTable(1L, null, 4, false);

	@Test
	void create() {
		final OrderLineItem 주문항목 = orderLineItem(1L, null, 일식메뉴, 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블, OrderStatus.COOKING, 주문항목목록);

		given(menuRepository.countByIdIn(any())).willReturn(1L);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(개별_주문테이블));
		given(orderRepository.save(any())).willReturn(주문);
		given(orderLineItemRepository.save(any())).willReturn(주문항목);

		final Order createdOrder = orderService.create(
			order(개별_주문테이블, Arrays.asList(orderLineItem(일식메뉴, 2)))
		);

		assertThat(createdOrder.getId()).isNotNull();
		assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(createdOrder.getOrderLineItems().size()).isEqualTo(1);
		assertThat(createdOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
	}

	@Test
	void create_empty_order_line_items() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(개별_주문테이블, Collections.emptyList())
			));
	}

	@Test
	void create_not_found_menu() {
		given(menuRepository.countByIdIn(any())).willReturn(0L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(개별_주문테이블, Arrays.asList(orderLineItem(null, 2)))
			));
	}

	@Test
	void create_not_found_order_table() {
		given(menuRepository.countByIdIn(any())).willReturn(1L);
		given(orderTableRepository.findById(any())).willReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(null, Arrays.asList(orderLineItem(일식메뉴, 2)))
			));
	}

	@Test
	void create_empty_order_table() {
		final OrderTable 빈_주문테이블 = orderTable(1L, null, 0, true);

		given(menuRepository.countByIdIn(any())).willReturn(1L);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(빈_주문테이블));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(
				order(빈_주문테이블, Arrays.asList(orderLineItem(일식메뉴, 2)))
			));
	}

	@Test
	void list() {
		final OrderLineItem 주문항목 = orderLineItem(1L, null, 일식메뉴, 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블, OrderStatus.COOKING, 주문항목목록);

		given(orderRepository.findAll()).willReturn(Arrays.asList(주문));
		given(orderLineItemRepository.findAllByOrderId(any())).willReturn(주문항목목록);

		final List<Order> orders = orderService.list();

		assertThat(orders).containsExactly(주문);
		assertThat(orders.get(0).getOrderLineItems()).containsExactly(주문항목);
	}

	@Test
	void changeOrderStatus() {
		final OrderLineItem 주문항목 = orderLineItem(1L, null, 일식메뉴, 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블, OrderStatus.COOKING, 주문항목목록);

		given(orderRepository.findById(any())).willReturn(Optional.of(주문));
		given(orderRepository.save(any())).willReturn(주문);
		given(orderLineItemRepository.findAllByOrderId(any())).willReturn(주문항목목록);

		final Order changedOrder = orderService.changeOrderStatus(주문.getId(), order(1L, OrderStatus.MEAL));

		assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	void changeOrderStatus_completion_order() {
		final OrderLineItem 주문항목 = orderLineItem(1L, null, 일식메뉴, 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블, OrderStatus.COMPLETION, 주문항목목록);

		given(orderRepository.findById(any())).willReturn(Optional.of(주문));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), order(1L, OrderStatus.COOKING)));
	}
}
