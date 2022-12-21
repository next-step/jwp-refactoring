package kitchenpos.order.application;

import static kitchenpos.order.ui.request.TableGroupRequest.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.ui.request.TableGroupRequest;
import kitchenpos.order.ui.response.TableGroupResponse;

@Service
public class TableGroupService {
	private final TableGroupRepository tableGroupRepository;
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public TableGroupService(
		final TableGroupRepository tableGroupRepository,
		final OrderRepository orderRepository,
		final OrderTableRepository orderTableRepository
	) {
		this.tableGroupRepository = tableGroupRepository;
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest request) {
		final TableGroup tableGroup = tableGroupRepository.save(newTableGroup(request));
		return TableGroupResponse.from(tableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup tableGroup = tableGroup(tableGroupId);
		validate(tableGroup);
		tableGroup.ungroup();
	}

	private TableGroup newTableGroup(TableGroupRequest request) {
		List<OrderTable> orderTables = orderTables(request.getOrderTables());
		return TableGroup.from(orderTables);
	}

	private List<OrderTable> orderTables(List<OrderTableIdRequest> requests) {
		return requests.stream()
			.map(request -> orderTable(request.getId()))
			.collect(Collectors.toList());
	}

	private OrderTable orderTable(long id) {
		return orderTableRepository.orderTable(id);
	}

	private void validate(TableGroup tableGroup) {
		if (isCookingOrMeal(tableGroup)) {
			throw new IllegalArgumentException("조리중이거나 식사중인 테이블은 테이블 그룹 해제할 수 없습니다.");
		}
	}

	private boolean isCookingOrMeal(TableGroup tableGroup) {
		return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
			tableGroup.orderTableIds(),
			Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
	}

	private TableGroup tableGroup(Long tableGroupId) {
		return tableGroupRepository.tableGroup(tableGroupId);
	}
}
