package kitchenpos.order.application;

import static kitchenpos.TestInstances.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.TestInstances;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	@InjectMocks
	private OrderService orderService;
	@Mock
	private MenuService menuService;
	@Mock
	private TableService tableService;
	@Mock
	private OrderRepository orderRepository;

	@BeforeEach
	void setUp() {
		TestInstances.init();
	}

	@DisplayName("주문 생성")
	@Test
	void create() {
		테이블1.changeEmpty(false);
		Map<Long, Menu> menus = new HashMap<>();
		menus.put(후라이드치킨메뉴.getId(), 후라이드치킨메뉴);
		menus.put(통구이메뉴.getId(), 통구이메뉴);

		when(menuService.findAllMenuByIds(anyList())).thenReturn(menus);
		when(tableService.findOrderTableById(anyLong())).thenReturn(테이블1);
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0, Order.class);
			ReflectionTestUtils.setField(order, "id", 1L);
			return order;
		});
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드치킨메뉴.getId(), 2);

		OrderResponse orderResponse = orderService.create(
			new OrderRequest(테이블1.getId(), Arrays.asList(orderLineItemRequest)));

		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse.getOrderLineItems()).map(OrderLineItemResponse::getOrderId).allMatch(Objects::nonNull);
	}

	@DisplayName("빈 테이블에는 주문 생성 실패")
	@Test
	void createWhenEmptyTable() {
		테이블1.changeEmpty(true);
		Map<Long, Menu> menus = new HashMap<>();
		menus.put(후라이드치킨메뉴.getId(), 후라이드치킨메뉴);
		menus.put(통구이메뉴.getId(), 통구이메뉴);

		when(menuService.findAllMenuByIds(anyList())).thenReturn(menus);
		when(tableService.findOrderTableById(anyLong())).thenReturn(테이블1);

		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드치킨메뉴.getId(), 2);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(new OrderRequest(테이블1.getId(), Arrays.asList(orderLineItemRequest))));
	}

	@DisplayName("주문 상태 변경")
	@Test
	void changeOrderStatus() {
		테이블1.changeEmpty(false);
		Map<Long, Menu> menus = new HashMap<>();
		menus.put(후라이드치킨메뉴.getId(), 후라이드치킨메뉴);
		menus.put(통구이메뉴.getId(), 통구이메뉴);
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드치킨메뉴.getId(), 2);

		when(menuService.findAllMenuByIds(anyList())).thenReturn(menus);
		when(tableService.findOrderTableById(anyLong())).thenReturn(테이블1);
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(
			Order.builder().orderTable(테이블1).build()));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0, Order.class);
			ReflectionTestUtils.setField(order, "id", 1L);
			return order;
		});

		OrderResponse orderResponse = orderService.create(
			new OrderRequest(테이블1.getId(), Arrays.asList(orderLineItemRequest)));

		OrderResponse changeResponse = orderService.changeOrderStatus(orderResponse.getId(),
			new OrderRequest(OrderStatus.MEAL));
		assertThat(changeResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@DisplayName("완료 상태시 주문 상태 변경 실패")
	@Test
	void changeOrderStatusWhenCompletion() {
		테이블1.changeEmpty(false);
		Map<Long, Menu> menus = new HashMap<>();
		menus.put(후라이드치킨메뉴.getId(), 후라이드치킨메뉴);
		menus.put(통구이메뉴.getId(), 통구이메뉴);
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(후라이드치킨메뉴.getId(), 2);

		when(menuService.findAllMenuByIds(anyList())).thenReturn(menus);
		when(tableService.findOrderTableById(anyLong())).thenReturn(테이블1);
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(
			Order.builder().orderTable(테이블1).orderStatus(OrderStatus.COMPLETION).build()));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0, Order.class);
			ReflectionTestUtils.setField(order, "id", 1L);
			return order;
		});

		OrderResponse orderResponse = orderService.create(
			new OrderRequest(테이블1.getId(), Arrays.asList(orderLineItemRequest)));
		
		assertThatIllegalArgumentException()
			.isThrownBy(
				() -> orderService.changeOrderStatus(orderResponse.getId(), new OrderRequest(OrderStatus.MEAL)));
	}
}
