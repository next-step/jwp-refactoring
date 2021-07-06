package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import kitchenpos.table.TableServiceTest;

@DisplayName("주문 서비스 테스트")
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

	Order 주문;
	OrderLineItem 주문항목;
	OrderTable 주문테이블;

	@BeforeEach
	void setUp() {
		주문항목 = 주문항목생성(1L, 1L, 1, 1L);
		주문테이블 = TableServiceTest.주문테이블생성(1L, 1L, false, 1);
		주문 = 주문생성(1L, LocalDateTime.now(), Arrays.asList(주문항목), 주문테이블.getId());
	}

	@DisplayName("주문을 생성한다.")
	@Test
	void 주문_생성() {
		given(menuDao.countByIdIn(Arrays.asList(주문항목.getMenuId()))).willReturn(1L);
		given(orderTableDao.findById(주문.getId())).willReturn(Optional.of(주문테이블));
		given(orderDao.save(주문)).willReturn(주문);
		given(orderLineItemDao.save(주문항목)).willReturn(주문항목);

		Order created = orderService.create(주문);

		주문_생성_확인(created, 주문);
	}

	@DisplayName("주문 생성 시 주문의 주문 항목이 1개 이상이어야 한다.")
	@Test
	void 주문_생성_시_주문의_주문_항목이_1개_이상이어야_한다() {
		주문.setOrderLineItems(Collections.EMPTY_LIST);

		assertThatThrownBy(() -> {
			orderService.create(주문);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 시 주문의 주문 항목들이 메뉴에 존재하지 않으면 생성할 수 없다.")
	@Test
	void 주문_생성_시_주문의_주문_항목들이_메뉴에_존재하지_않으면_생성할_수_없다() {
		given(menuDao.countByIdIn(Arrays.asList(주문항목.getMenuId()))).willReturn(2L);

		assertThatThrownBy(() -> {
			orderService.create(주문);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 시 주문의 주문 테이블이 존재하지 않으면 생성할 수 없다")
	@Test
	void 주문_생성_시_주문의_주문_테이블이_존재하지_않으면_생성할_수_없다() {
		given(menuDao.countByIdIn(Arrays.asList(주문항목.getMenuId()))).willReturn(1L);
		given(orderTableDao.findById(주문.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			orderService.create(주문);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 시 주문의 주문 테이블이 빈 테이블이 있으면 생성할 수 없다.")
	@Test
	void 주문_생성_시_주문의_주문_테이블이_빈_테이블이_있으면_생성할_수_없다() {
		주문테이블.setEmpty(true);
		given(menuDao.countByIdIn(Arrays.asList(주문항목.getMenuId()))).willReturn(1L);
		given(orderTableDao.findById(주문.getId())).willReturn(Optional.of(주문테이블));

		assertThatThrownBy(() -> {
			orderService.create(주문);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 리스트를 조회한다.")
	@Test
	void 주문_리스트_조회() {
		given(orderDao.findAll()).willReturn(Arrays.asList(주문));
		given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Arrays.asList(주문항목));

		List<Order> selectedOrders = orderService.list();

		주문_리스트_조회_확인(selectedOrders, Arrays.asList(주문));
	}

	@DisplayName("주문 상태를 변경한다.")
	@Test
	void 주문_상태_변경() {
		주문.setOrderStatus(OrderStatus.MEAL.name());
		given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

		Order changedStatusOrder = orderService.changeOrderStatus(주문.getId(), 주문);

		주문_상태_변경_확인(changedStatusOrder, OrderStatus.MEAL);
	}

	@DisplayName("주문 상태를 변경 - 변경할 주문의 아이디에 해당하는 주문이 없으면 변경할 수 없다.")
	@Test
	void 주문_상태_변경_변경할_주문의_아이디에_해당하는_주문이_없으면_변경할_수_없다() {
		given(orderDao.findById(주문.getId())).willReturn(Optional.ofNullable(null));
		assertThatThrownBy(() -> {
			Order changedStatusOrder = orderService.changeOrderStatus(주문.getId(), 주문);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태를 변경 - 계산완료된 주문은 상태를 변경할 수 없다.")
	@Test
	void 주문_상태_변경_계산완료된_주문은_상태를_변경할_수_없다() {
		주문.setOrderStatus(OrderStatus.COMPLETION.name());
		given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));
		assertThatThrownBy(() -> {
			Order changedStatusOrder = orderService.changeOrderStatus(주문.getId(), 주문);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	void 주문_상태_변경_확인(Order changedStatusOrder, OrderStatus expectedStatus) {
		assertThat(changedStatusOrder.getOrderStatus()).isEqualTo(expectedStatus.name());
	}

	void 주문_리스트_조회_확인(List<Order> selectedOrder, List<Order> expected) {
		assertThat(selectedOrder).containsAll(expected);
	}

	void 주문_생성_확인(Order created, Order expected) {
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(created.getOrderTableId()).isEqualTo(expected.getOrderTableId());
		assertThat(created.getOrderLineItems()).containsAll(expected.getOrderLineItems());
		assertThat(created.getOrderedTime()).isEqualTo(expected.getOrderedTime());
	}

	public static OrderLineItem 주문항목생성(Long orderId, Long menuId, int quantity, Long seq) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setOrderId(orderId);
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(quantity);
		orderLineItem.setSeq(seq);
		return orderLineItem;
	}

	public static Order 주문생성(Long id, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems,
		Long orderTableId) {
		Order order = new Order();
		order.setId(id);
		order.setOrderedTime(orderedTime);
		order.setOrderLineItems(orderLineItems);
		order.setOrderTableId(orderTableId);
		return order;
	}
}
