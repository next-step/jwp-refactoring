package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import java.time.LocalDateTime;
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

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.domain.TestDomainConstructor;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private OrderLineItemDao orderLineItemDao;
	@Mock
	private OrderDao orderDao;
	@InjectMocks
	private OrderService orderService;

	private static final Long NEW_ORDER_ID = 1L;
	private static final Long NOT_EMPTY_TABLE_ID = 9L;
	private static final Long MENU_ID = 1L;
	private static final Long MENU2_ID = 2L;
	private static final List<Long> MENU_IDS = Arrays.asList(MENU_ID, MENU2_ID);
	private OrderTable savedOrderTable;
	private OrderLineItem orderLineItem;
	private OrderLineItem orderLineItem2;
	private List<OrderLineItem> orderLineItems;
	private Order createdOrder;
	private long orderLineItemsSize;

	@BeforeEach
	void setUp() {
		savedOrderTable = TestDomainConstructor.orderTableWithId(1L, 2, false, NOT_EMPTY_TABLE_ID);
		orderLineItem = TestDomainConstructor.orderLineItem(null, MENU_ID, 1);
		orderLineItem2 = TestDomainConstructor.orderLineItem(null, MENU2_ID, 2);
		orderLineItems = Arrays.asList(orderLineItem, orderLineItem2);
		orderLineItemsSize = orderLineItems.size();
		createdOrder = TestDomainConstructor.orderWithId(NOT_EMPTY_TABLE_ID, OrderStatus.COOKING.name(), LocalDateTime
			.now(), orderLineItems, NEW_ORDER_ID);
	}

	@Test
	@DisplayName("주문을 등록할 수 있다.")
	void create() {
		//given
		String orderStatus = OrderStatus.COOKING.name();
		Order order = TestDomainConstructor.order(NOT_EMPTY_TABLE_ID, null, null, orderLineItems);
		OrderLineItem savedOrderLineItem = TestDomainConstructor.orderLineItemWithSeq(NEW_ORDER_ID, MENU_ID, 1, 1L);
		OrderLineItem savedOrderLineItem2 = TestDomainConstructor.orderLineItemWithSeq(NEW_ORDER_ID, MENU2_ID, 1, 2L);

		when(menuRepository.countByIdIn(MENU_IDS)).thenReturn(orderLineItemsSize);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));
		//save 전에 3가지 정보가 setting 되어야 함
		when(orderDao.save(argThat(allOf(
			hasProperty("orderTableId", is(NOT_EMPTY_TABLE_ID))
			, hasProperty("orderStatus", is(orderStatus))
			, hasProperty("orderedTime", notNullValue())
		)))).thenReturn(createdOrder);
		when(orderLineItemDao.save(any())).thenReturn(savedOrderLineItem, savedOrderLineItem2);

		//when
		Order result = orderService.create(order);

		//then
		assertThat(result.getId()).isEqualTo(NEW_ORDER_ID);
		assertThat(result.getOrderStatus()).isEqualTo(orderStatus);
		assertThat(result.getOrderedTime()).isNotNull();
		assertThat(result.getOrderTableId()).isEqualTo(NOT_EMPTY_TABLE_ID);
		assertThat(result.getOrderLineItems()).containsExactlyInAnyOrder(savedOrderLineItem, savedOrderLineItem2);
	}

	@Test
	@DisplayName("주문 등록 시, 주문 아이템이 Null이면 IllegalArgumentException을 throw 해야한다.")
	void createOrderItemNull() {
		//given
		Order nullItemOrder = TestDomainConstructor.order(NOT_EMPTY_TABLE_ID, null, null, null);

		//when-then
		assertThatThrownBy(() -> orderService.create(nullItemOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 주문 아이템이 0개면 IllegalArgumentException을 throw 해야한다.")
	void createOrderItemEmpty() {
		//given
		Order emptyItemOrder = TestDomainConstructor.order(NOT_EMPTY_TABLE_ID, null, null, new ArrayList<>());

		//when-then
		assertThatThrownBy(() -> orderService.create(emptyItemOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 주문 테이블이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createNotExistOrderTable() {
		//given
		Order notExistOrderTable = TestDomainConstructor.order(100L, null, null, orderLineItems);
		when(menuRepository.countByIdIn(MENU_IDS)).thenReturn(orderLineItemsSize);
		when(orderTableRepository.findById(100L)).thenReturn(Optional.empty());

		//when-then
		assertThatThrownBy(() -> orderService.create(notExistOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 메뉴가 모두 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createNotExistMenus() {
		//given
		Order notExistMenus = TestDomainConstructor.order(NOT_EMPTY_TABLE_ID, null, null, orderLineItems);
		when(menuRepository.countByIdIn(MENU_IDS)).thenReturn(1L);

		//when-then
		assertThatThrownBy(() -> orderService.create(notExistMenus))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 주문테이블이 빈 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void createPriceLessThanZero() {
		//given
		OrderTable emptyTable = TestDomainConstructor.orderTableWithId(null, 0, true, 3L);
		Order emptyTableOrder = TestDomainConstructor.order(3L, null, null, orderLineItems);
		when(menuRepository.countByIdIn(MENU_IDS)).thenReturn(orderLineItemsSize);
		when(orderTableRepository.findById(3L)).thenReturn(Optional.of(emptyTable));

		//when-then
		assertThatThrownBy(() -> orderService.create(emptyTableOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문의 목록을 주문의 상품목록과 함께 조회할 수 있다.")
	void list() {
		//given
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		Order orderWithTwoItems = TestDomainConstructor.orderWithId(NOT_EMPTY_TABLE_ID, null, null
			, Arrays.asList(orderLineItem, orderLineItem), 1L);
		Order orderWithThreeItems = TestDomainConstructor.orderWithId(NOT_EMPTY_TABLE_ID, null, null
			, Arrays.asList(orderLineItem, orderLineItem, orderLineItem), 2L);

		when(orderDao.findAll()).thenReturn(Arrays.asList(orderWithTwoItems, orderWithThreeItems));
		when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(orderWithTwoItems.getOrderLineItems(), orderWithThreeItems.getOrderLineItems());

		//when
		List<Order> results = orderService.list();

		//then
		assertThat(results.size()).isEqualTo(2);
		assertThat(results.get(0).getOrderLineItems().size()).isEqualTo(2);
		assertThat(results.get(1).getOrderLineItems().size()).isEqualTo(3);
	}

	@Test
	@DisplayName("주문 상태를 변경할 수 있다.")
	void changeOrderStatus() {
		//given
		String changedStatus = OrderStatus.MEAL.name();
		Order changeStatusOrder = TestDomainConstructor.order(NOT_EMPTY_TABLE_ID, changedStatus, null, orderLineItems);
		when(orderDao.findById(NEW_ORDER_ID)).thenReturn(Optional.of(createdOrder));
		when(orderLineItemDao.findAllByOrderId(NEW_ORDER_ID)).thenReturn(orderLineItems);

		//when
		Order result = orderService.changeOrderStatus(NEW_ORDER_ID, changeStatusOrder);

		//then
		assertThat(result.getOrderStatus()).isEqualTo(changedStatus);
		assertThat(result.getOrderLineItems().size()).isEqualTo(orderLineItems.size());
	}

	@Test
	@DisplayName("주문 상태를 변경 시, 주문이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void changeNotExistOrderStatus() {
		//given
		Long notExistOrderId = 200L;
		Order changeStatusOrder = mock(Order.class);
		when(orderDao.findById(notExistOrderId)).thenReturn(Optional.empty());

		//when-then
		assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, changeStatusOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 상태를 변경 시, 주문 상태가 계산 완료인 경우 변경할 수 없다.")
	void changeCompleteStatus() {
		//given
		Long completeStatusOrderId = 300L;
		Order changeStatusOrder = TestDomainConstructor.order(NOT_EMPTY_TABLE_ID, OrderStatus.MEAL.name(), null, orderLineItems);
		Order completeStatusOrder = TestDomainConstructor.orderWithId(NOT_EMPTY_TABLE_ID, OrderStatus.COMPLETION.name(), null, orderLineItems, completeStatusOrderId);
		when(orderDao.findById(completeStatusOrderId)).thenReturn(Optional.of(completeStatusOrder));

		//when-then
		assertThatThrownBy(() -> orderService.changeOrderStatus(completeStatusOrderId, changeStatusOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
