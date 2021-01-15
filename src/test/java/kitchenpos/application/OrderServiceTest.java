package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private TableService tableService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private OrderDao orderDao;

	private OrderTableResponse orderTable;
	private OrderLineItemRequest request1;
	private OrderLineItemRequest request2;

	@BeforeEach
	void setUp() {
		orderTable = tableService.create(new OrderTableRequest_Create(20, false));
		request1 = createRequest("삼선짜장", 5000, 2);
		request2 = createRequest("삼선짬뽕", 6000, 2);
	}

	private OrderLineItemRequest createRequest(String menuName, int price, int quantity) {
		MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("음식"));
		ProductResponse product = productService.create(new ProductRequest(menuName, new BigDecimal(price)));
		MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1);
		MenuResponse menu = menuService.create(new MenuRequest("짜장면", new BigDecimal(price), menuGroup.getId(),
				Collections.singletonList(menuProductRequest)));
		return new OrderLineItemRequest(menu.getId(), quantity);
	}

	@DisplayName("새로운 주문을 생성한다.")
	@Test
	void create() {
		// when
		OrderResponse orderResponse = orderService.create(new OrderRequest_Create(Arrays.asList(request1, request2),
				orderTable.getId()));

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse.getOrderTableId()).isEqualTo(orderTable.getId());
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
		assertThat(orderResponse.getOrderLineItems())
				.hasSize(2)
				.allSatisfy(lineItem -> assertThat(lineItem.getOrderId()).isEqualTo(orderResponse.getId()))
				.map(OrderLineItemResponse::getQuantity)
				.allSatisfy(quantity -> assertThat(quantity).isEqualTo(2));
	}

	@DisplayName("주문 생성시 인자에 메뉴가 없을 경우 예외 발생.")
	@Test
	void create_EmptyOrderLineItems() {
		assertThatThrownBy(() -> orderService.create(new OrderRequest_Create(Collections.emptyList(), orderTable.getId())))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 실제 존재하지 않는 메뉴를 인자로 했을 경우 예외 발생.")
	@Test
	void create_NotExistOrderLineItems() {
		OrderLineItemRequest wrongItemRequest = new OrderLineItemRequest(-1, 1);

		assertThatThrownBy(() -> orderService.create(
				new OrderRequest_Create(Collections.singletonList(wrongItemRequest), orderTable.getId())))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 실제 존재하지 않는 테이블을 인자로 했을 경우 예외 발생.")
	@Test
	void create_NotExistOrderTable() {

		assertThatThrownBy(() -> orderService.create(new OrderRequest_Create(Arrays.asList(request1, request2), -5)))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 테이블이 비어있을 경우 예외 발생.")
	@Test
	void create_EmptyOrderTable() {
		tableService.changeEmpty(orderTable.getId(), new OrderTableRequest_ChangeEmpty(true));
		assertThatThrownBy(() -> orderService.create(new OrderRequest_Create(Arrays.asList(request1, request2), orderTable.getId())))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("모든 주문을 조회한다.")
	@Test
	void list() {
		OrderResponse orderResponse1 = orderService.create(new OrderRequest_Create(Arrays.asList(request1, request2),
				orderTable.getId()));
		OrderResponse orderResponse2 = orderService.create(new OrderRequest_Create(Arrays.asList(request1, request2),
				orderTable.getId()));

		assertThat(orderService.list())
				.map(OrderResponse::getId)
				.contains(orderResponse1.getId(), orderResponse2.getId());
	}

	@DisplayName("주문의 상태를 바꾼다.")
	@Test
	void changeOrderStatus() {
		OrderResponse orderResponse = orderService.create(new OrderRequest_Create(Arrays.asList(request1, request2),
				orderTable.getId()));

		orderService.changeOrderStatus(orderResponse.getId(), new OrderRequest_ChangeStatus(OrderStatus.MEAL.name()));

		assertThat(orderDao.findById(orderResponse.getId())).isPresent()
				.get()
				.extracting(Order::getOrderStatus)
				.isEqualTo(OrderStatus.MEAL.name());
	}

	@DisplayName("주문 상태 변경시 이미 완료된 주문을 바꿀시 예외 발생.")
	@Test
	void changeOrderStatus_StatusWrong() {
		// given
		OrderResponse orderResponse = orderService.create(new OrderRequest_Create(Arrays.asList(request1, request2),
				orderTable.getId()));
		orderService.changeOrderStatus(orderResponse.getId(), new OrderRequest_ChangeStatus(OrderStatus.COMPLETION.name()));

		assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), new OrderRequest_ChangeStatus(OrderStatus.MEAL.name())))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
