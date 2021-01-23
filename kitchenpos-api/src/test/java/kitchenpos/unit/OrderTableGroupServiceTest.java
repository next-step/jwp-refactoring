package kitchenpos.unit;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.OrderTableGroupService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.order.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrderTableGroupServiceTest {

	@Autowired
	private OrderTableGroupService orderTableGroupService;

	@Autowired
	private OrderService orderService;

	@Test
	@DisplayName("테이블 그룹을 저장한다")
	void createTableGroup() {
		Set<Long> orderTableIds = new HashSet<>();
		orderTableIds.add(1L);
		orderTableIds.add(2L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
		TableGroupResponse tableGroupResponse = orderTableGroupService.createOrderTable(tableGroupRequest);
		assertThat(tableGroupResponse.getOrderTables().size()).isEqualTo(2);
	}

	@Test
	@DisplayName("테이블 그룹을 저장 시 테이블은 2개 이상이어야함")
	void whenSaveTableGroupHaveToUpperTwoTables() {
		Set<Long> orderTableIds = new HashSet<>();
		orderTableIds.add(1L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
		assertThrows(IllegalArgumentException.class, () -> orderTableGroupService.createOrderTable(tableGroupRequest));
	}

	@Test
	@DisplayName("테이블 그룹을 삭제한다")
	void ungroup() {
		Set<Long> orderTableIds = new HashSet<>();
		orderTableIds.add(1L);
		orderTableIds.add(2L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
		TableGroupResponse tableGroupResponse = orderTableGroupService.createOrderTable(tableGroupRequest);
		assertThat(tableGroupResponse.getOrderTables().size()).isEqualTo(2);

		orderTableGroupService.ungroup(tableGroupResponse.getId());
	}

	void createChangeOrderStatus() {
		List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2L);
		orderLineItemRequests.add(orderLineItemRequest);
		OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

		OrderResponse orderResponse = orderService.createOrder(orderRequest);
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

		OrderRequest orderRequest2 = new OrderRequest(OrderStatus.MEAL.name());
		OrderResponse orderResponse2 = orderService.changeOrderStatus(orderResponse.getId(), orderRequest2);
		assertThat(orderResponse2.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	@DisplayName("조리 식사 상태의 테이블은 삭제할 수 없다")
	void givenMealStatusWhenUngroupThenError() {
		this.createChangeOrderStatus();
		assertThrows(IllegalArgumentException.class, () ->orderTableGroupService.ungroup(1L));
	}

}
