package kitchenpos.orders.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.orders.dto.OrderLineItemRequest;
import kitchenpos.orders.dto.OrderRequest;
import kitchenpos.orders.dto.OrderResponse;

@Transactional
@SpringBootTest
class OrderServiceTest {

	private static OrderRequest changeOrderStatus = new OrderRequest("COMPLETION");

	@Autowired
	private OrderService orderService;

	@Test
	@DisplayName("주문 생성 테스트")
	public void createOrderSuccessTest() {
		//given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
		OrderRequest orderRequest = new OrderRequest(7L, Lists.newArrayList(orderLineItemRequest));

		//when
		OrderResponse save = orderService.create(orderRequest);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(8L);
	}

	@Test
	@DisplayName("메뉴를 고르지 않아서 생성 실패")
	public void createOrderFailNotSelectMenuTest() {
		//given
		OrderRequest orderRequest = new OrderRequest(1L, Lists.emptyList());

		//when
		//then
		assertThatThrownBy(() -> orderService.create(orderRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문할 메뉴를 골라주세요");
	}

	@Test
	@DisplayName("식당에 없는 메뉴를 주문해서 실패")
	public void createOrderFailNoneMenuTest() {
		//given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(99L, 2);
		OrderRequest orderRequest = new OrderRequest(1L, Lists.newArrayList(orderLineItemRequest));

		//when
		//then
		assertThatThrownBy(() -> orderService.create(orderRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴가 존재하지 않습니다");
	}

	@Test
	@DisplayName("주문을 받을 테이블이 없어서 실패")
	public void createOrderFailNotExistedTableTest() {
		//given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
		OrderRequest orderRequest = new OrderRequest(99L, Lists.newArrayList(orderLineItemRequest));

		//when
		//then
		assertThatThrownBy(() -> orderService.create(orderRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("테이블이 존재하지 않습니다");
	}

	@Test
	@DisplayName("주문이 할당 된 테이블이 비어있어서 실패")
	public void createOrderFailEmptyTableTest() {
		//given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
		OrderRequest orderRequest = new OrderRequest(1L, Lists.newArrayList(orderLineItemRequest));

		//when
		//then
		assertThatThrownBy(() -> orderService.create(orderRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("테이블이 비어있습니다");
	}

	@Test
	@DisplayName("주문 목록 조회 테스트")
	public void findAllOrderList() {
		//given
		//when
		List<OrderResponse> orders = orderService.list();

		//then
		assertThat(orders).hasSize(7);
	}

	@Test
	@DisplayName("주문 상태 변경 (식사중 -> 계산완료) 테스트")
	public void changeOrderStatusTest() {
		//given
		//when
		OrderResponse change = orderService.changeOrderStatus(1L, changeOrderStatus);

		//then
		assertThat(change.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
	}

	@Test
	@DisplayName("존재하지 않는 주문 상태 변경 실패 테스트")
	public void changeOrderStatusFailNotExistedTest() {
		assertThatThrownBy(() -> orderService.changeOrderStatus(99L, changeOrderStatus))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문이 존재하지 않습니다");
	}

	@Test
	@DisplayName("계산완료 된 주문 상태변경 실패")
	public void changeOrderStatusFailAlreadyCompletionTest() {
		assertThatThrownBy(() -> orderService.changeOrderStatus(5L, changeOrderStatus))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("계산완료 된 주문은 상태를 변경 할 수 없습니다");
	}
}
