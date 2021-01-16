package kitchenpos.unit;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.OrderTableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class OrderTableGroupServiceTest {

	@Autowired
	private OrderTableGroupService orderTableGroupService;

	@Autowired
	private OrderService orderService;

	@Test
	@DisplayName("테이블 그룹을 저장한다")
	void create() {
		Set<Long> orderTableIds = new HashSet<>();
		orderTableIds.add(1L);
		orderTableIds.add(2L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
		TableGroupResponse tableGroupResponse = orderTableGroupService.create(tableGroupRequest);
		assertThat(tableGroupResponse.getOrderTables().size()).isEqualTo(2);
	}

	@Test
	@DisplayName("테이블 그룹을 저장 시 테이블은 2개 이상이어야함")
	void whenSaveTableGroupHaveToUpperTwoTables() {
		Set<Long> orderTableIds = new HashSet<>();
		orderTableIds.add(1L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);
		assertThrows(IllegalArgumentException.class, () -> orderTableGroupService.create(tableGroupRequest));
	}

	@Test
	@DisplayName("테이블 그룹을 삭제한다")
	void ungroup() {
		this.create();
		orderTableGroupService.ungroup(1L);
	}

	void createChangeOrderStatus() {
		List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2);
		orderLineItemRequests.add(orderLineItemRequest);
		OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

		OrderResponse orderResponse = orderService.create(orderRequest);
		assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

		OrderRequest orderRequest2 = new OrderRequest(OrderStatus.MEAL.name());
		OrderResponse orderResponse2 = orderService.changeOrderStatus(1L, orderRequest2);
		assertThat(orderResponse2.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	@DisplayName("조리 식사 상태의 테이블은 삭제할 수 없다")
	void givenMealStatusWhenUngroupThenError() {
		this.createChangeOrderStatus();
		assertThrows(IllegalArgumentException.class, () ->orderTableGroupService.ungroup(1L));
	}

}
