package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
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
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.TableServiceTest;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private TableRepository tableRepository;
	@InjectMocks
	private OrderService orderService;

	MenuGroup 치킨;
	Menu 양념반_후라이드반;
	Product 양념치킨;
	Product 후라이드치킨;
	MenuProduct 양념_반_치킨;
	MenuProduct 후라이드_반_치킨;

	Order 주문;
	OrderLineItem 주문항목1;
	OrderLineItem 주문항목2;
	OrderLineItems 주문항목들;
	OrderTable 주문테이블;

	@BeforeEach
	void setUp() {
		치킨 = new MenuGroup(1L, "치킨");
		양념치킨 = new Product(1L, "양념치킨", new Price(BigDecimal.valueOf(1000)));
		후라이드치킨 = new Product(2L, "후라이드치킨", new Price(BigDecimal.valueOf(1000)));
		양념_반_치킨 = new MenuProduct(1L, 양념치킨, new Quantity(1));
		후라이드_반_치킨 = new MenuProduct(2L, 후라이드치킨, new Quantity(1));
		양념반_후라이드반 = new Menu(1L, "양념반 후라이드반", new Price(BigDecimal.valueOf(2000)), 치킨);

		주문항목1 = 주문항목생성(양념반_후라이드반, new Quantity(1), 1L);
		주문항목2 = 주문항목생성(양념반_후라이드반, new Quantity(1), 2L);
		주문테이블 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		주문항목들 = new OrderLineItems(Arrays.asList(주문항목1, 주문항목2));
		주문 = 주문생성(1L, 주문테이블, 주문항목들);
	}

	@DisplayName("주문을 생성한다.")
	@Test
	void 주문_생성() {
		OrderLineItemRequest 주문항목_요청1 = new OrderLineItemRequest(1L, 1);
		OrderLineItemRequest 주문항목_요청2 = new OrderLineItemRequest(2L, 1);
		OrderRequest 주문_요청 = new OrderRequest(1L, Arrays.asList(주문항목_요청1, 주문항목_요청2));
		given(tableRepository.findById(주문.getId())).willReturn(Optional.of(주문테이블));
		given(orderRepository.save(any())).willReturn(주문);

		OrderResponse created = orderService.create(주문_요청);

		주문_생성_확인(created);
	}



	@DisplayName("주문 생성 시 주문의 주문 테이블이 존재하지 않으면 생성할 수 없다")
	@Test
	void 주문_생성_시_주문의_주문_테이블이_존재하지_않으면_생성할_수_없다() {
		OrderLineItemRequest 주문항목_요청1 = new OrderLineItemRequest(1L, 1);
		OrderLineItemRequest 주문항목_요청2 = new OrderLineItemRequest(2L, 1);
		OrderRequest 주문_요청 = new OrderRequest(1L, Arrays.asList(주문항목_요청1, 주문항목_요청2));
		given(tableRepository.findById(주문.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			orderService.create(주문_요청);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 생성 시 주문의 주문 테이블이 빈 테이블이 있으면 생성할 수 없다.")
	@Test
	void 주문_생성_시_주문의_주문_테이블이_빈_테이블이_있으면_생성할_수_없다() {
		OrderLineItemRequest 주문항목_요청1 = new OrderLineItemRequest(1L, 1);
		OrderLineItemRequest 주문항목_요청2 = new OrderLineItemRequest(2L, 1);
		OrderRequest 주문_요청 = new OrderRequest(1L, Arrays.asList(주문항목_요청1, 주문항목_요청2));
		주문테이블 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), true);
		given(tableRepository.findById(주문.getId())).willReturn(Optional.of(주문테이블));

		assertThatThrownBy(() -> {
			orderService.create(주문_요청);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 리스트를 조회한다.")
	@Test
	void 주문_리스트_조회() {
		given(orderRepository.findAll()).willReturn(Arrays.asList(주문));

		List<OrderResponse> selectedOrders = orderService.list();

		주문_리스트_조회_확인(selectedOrders);
	}

	@DisplayName("주문 상태를 변경한다.")
	@Test
	void 주문_상태_변경() {
		OrderStatusChangeRequest 주문_상태_변경_요청 = new OrderStatusChangeRequest(OrderStatus.MEAL.name());
		given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

		OrderResponse changedStatusOrder = orderService.changeOrderStatus(주문.getId(), 주문_상태_변경_요청);

		주문_상태_변경_확인(changedStatusOrder, OrderStatus.MEAL);
	}

	@DisplayName("주문 상태를 변경 - 변경할 주문의 아이디에 해당하는 주문이 없으면 변경할 수 없다.")
	@Test
	void 주문_상태_변경_변경할_주문의_아이디에_해당하는_주문이_없으면_변경할_수_없다() {
		OrderStatusChangeRequest 주문_상태_변경_요청 = new OrderStatusChangeRequest(OrderStatus.MEAL.name());
		given(orderRepository.findById(주문.getId())).willReturn(Optional.ofNullable(null));
		assertThatThrownBy(() ->
			orderService.changeOrderStatus(주문.getId(), 주문_상태_변경_요청)
		).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태를 변경 - 계산완료된 주문은 상태를 변경할 수 없다.")
	@Test
	void 주문_상태_변경_계산완료된_주문은_상태를_변경할_수_없다() {
		주문.changeOrderStatus(OrderStatus.COMPLETION);
		OrderStatusChangeRequest 주문_상태_변경_요청 = new OrderStatusChangeRequest(OrderStatus.MEAL.name());
		given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));
		assertThatThrownBy(() ->
			orderService.changeOrderStatus(주문.getId(), 주문_상태_변경_요청)
		).isInstanceOf(OrderException.class);
	}

	void 주문_상태_변경_확인(OrderResponse changedStatusOrder, OrderStatus expectedStatus) {
		assertThat(changedStatusOrder.getOrderStatus()).isEqualTo(expectedStatus.name());
	}

	void 주문_리스트_조회_확인(List<OrderResponse> selectedOrder) {
		assertThat(selectedOrder).isNotNull();
		assertThat(selectedOrder).isNotEmpty();
	}

	void 주문_생성_확인(OrderResponse created) {
		assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(created.getOrderTableResponse()).isNotNull();
	}

	public static OrderLineItem 주문항목생성(Menu menu, Quantity quantity, Long seq) {
		OrderLineItem orderLineItem = new OrderLineItem(seq, menu, quantity);
		return orderLineItem;
	}

	public static Order 주문생성(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
		Order order = new Order(id, orderTable);
		return order;
	}
}
