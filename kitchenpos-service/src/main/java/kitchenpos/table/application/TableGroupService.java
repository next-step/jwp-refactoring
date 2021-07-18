package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
	private final OrderService orderService;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(OrderService orderService, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
		this.orderService = orderService;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
		if (orderTables.size() != tableGroupRequest.getOrderTableIdsSize()) {
			throw new IllegalArgumentException("주문 테이블 숫자가 일치하지 않습니다.");
		}

		TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());

		List<OrderTable> changedOrderTables = orderTables.stream().map(orderTable -> {
			orderTable.addTableGroupId(tableGroup.getId());
			orderTable.fillTable();
			return orderTable;
		}).collect(Collectors.toList());

		tableGroup.addOrderTables(changedOrderTables);
		return TableGroupResponse.of(tableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
		List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId)
				.collect(Collectors.toList());

		orderService.checkProcessingOrders(orderTableIds);
		orderTables.forEach(OrderTable::removeTableGroupId);
	}
}
