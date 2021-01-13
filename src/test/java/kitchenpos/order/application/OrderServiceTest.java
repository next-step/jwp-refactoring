package kitchenpos.order.application;

import static kitchenpos.utils.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.BaseServiceTest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.domain.OrderStatus;

public class OrderServiceTest extends BaseServiceTest {
	@Autowired
	private OrderService orderService;

	private static final Long 주문가능한_TABLE_ID = 테이블_비어있지않은_2명_9.getId();
	private static final OrderRequest CHANGE_STATUS_REQUEST = new OrderRequest(OrderStatus.MEAL.name());
	private OrderLineItemRequest orderLineItemRequest;
	private OrderLineItemRequest orderLineItemRequest2;
	private List<OrderLineItemRequest> orderLineItemRequests;

	@BeforeEach
	public void setUp() {
		orderLineItemRequest = new OrderLineItemRequest(메뉴_후라이드.getId(), 1);
		orderLineItemRequest2 = new OrderLineItemRequest(메뉴_양념치킨.getId(), 1);
		orderLineItemRequests = Arrays.asList(orderLineItemRequest, orderLineItemRequest2);
	}

	@Test
	@DisplayName("주문을 등록할 수 있다.")
	void create() {
		//given
		OrderRequest orderRequest = new OrderRequest(주문가능한_TABLE_ID, 주문_신규_주문상태, orderLineItemRequests);

		//when
		OrderResponse result = orderService.create(orderRequest);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getOrderStatus()).isEqualTo(주문_신규_주문상태);
		assertThat(result.getOrderedTime()).isNotNull();
		assertThat(result.getOrderTableId()).isEqualTo(주문_신규_테이블_ID);
		assertThat(result.getOrderLineItems().size()).isEqualTo(2);
		assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(result.getId());
		assertThat(result.getOrderLineItems().get(0).getMenuId()).isEqualTo(메뉴_후라이드.getId());
		assertThat(result.getOrderLineItems().get(1).getOrderId()).isEqualTo(result.getId());
		assertThat(result.getOrderLineItems().get(1).getMenuId()).isEqualTo(메뉴_양념치킨.getId());
	}

	@Test
	@DisplayName("주문 등록 시, 주문 아이템이 Null이면 IllegalArgumentException을 throw 해야한다.")
	void createOrderItemNull() {
		//given
		OrderRequest nullItemOrder = new OrderRequest(주문가능한_TABLE_ID, 주문_신규_주문상태, null);

		//when-then
		assertThatThrownBy(() -> orderService.create(nullItemOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 주문 아이템이 0개면 IllegalArgumentException을 throw 해야한다.")
	void createOrderItemEmpty() {
		//given
		OrderRequest emptyItemOrder = new OrderRequest(주문가능한_TABLE_ID, 주문_신규_주문상태, new ArrayList<>());

		//when-then
		assertThatThrownBy(() -> orderService.create(emptyItemOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 주문 테이블이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createNotExistOrderTable() {
		//given
		OrderRequest notExistOrderTable = new OrderRequest(존재하지않는_ID, 주문_신규_주문상태, null);

		//when-then
		assertThatThrownBy(() -> orderService.create(notExistOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 메뉴가 모두 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createNotExistMenus() {
		//given
		OrderLineItemRequest notExistMenus = new OrderLineItemRequest(존재하지않는_ID, 1);
		OrderRequest notExistMenusOrder = new OrderRequest(주문가능한_TABLE_ID, 주문_신규_주문상태, Arrays.asList(notExistMenus));

		//when-then
		assertThatThrownBy(() -> orderService.create(notExistMenusOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 등록 시, 주문테이블이 빈 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void createPriceLessThanZero() {
		//given
		OrderRequest emptyTableOrder = new OrderRequest(테이블_비어있는_0명_1.getId(), 주문_신규_주문상태, orderLineItemRequests);

		//when-then
		assertThatThrownBy(() -> orderService.create(emptyTableOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문의 목록을 주문의 상품목록과 함께 조회할 수 있다.")
	void list() {
		//when
		List<OrderResponse> results = orderService.list();

		//then
		assertThat(results).isNotEmpty();
		assertThat(results.get(0).getOrderLineItems()).isNotEmpty();
		assertThat(results.get(0).getOrderLineItems().get(0).getSeq()).isNotNull();
	}

	@Test
	@DisplayName("주문 상태를 변경할 수 있다.")
	void changeOrderStatus() {
		//when
		OrderResponse result = orderService.changeOrderStatus(주문_조리중_테이블11.getId(), CHANGE_STATUS_REQUEST);

		//then
		assertThat(result.getId()).isEqualTo(주문_조리중_테이블11.getId());
		assertThat(result.getOrderStatus()).isEqualTo(CHANGE_STATUS_REQUEST.getOrderStatus());
	}

	@Test
	@DisplayName("주문 상태를 변경 시, 주문이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void changeNotExistOrderStatus() {
		//when-then
		assertThatThrownBy(() -> orderService.changeOrderStatus(존재하지않는_ID, CHANGE_STATUS_REQUEST))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 상태를 변경 시, 주문 상태가 계산 완료인 경우 변경할 수 없다.")
	void changeCompleteStatus() {
		//when-then
		assertThatThrownBy(() -> orderService.changeOrderStatus(주문_계산완료_테이블13.getId(), CHANGE_STATUS_REQUEST))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
