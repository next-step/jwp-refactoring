package kitchenpos.tablegroup.application;

import kitchenpos.orders.application.OrderService;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
	private final OrderService orderService;
	private final OrderTableService orderTableService;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(OrderService orderService,
		OrderTableService orderTableService, TableGroupRepository tableGroupRepository) {
		this.orderService = orderService;
		this.orderTableService = orderTableService;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroup create(final TableGroupRequest tableGroupRequest) {
		validateOrderTableSizeLessThanTwo(tableGroupRequest);

		List<OrderTable> orderTables = orderTableService.findOrderTableByIdIn(extractOrderTableIds(tableGroupRequest));

		validateOrderTableIsUseOrIsGrouped(orderTables);

		TableGroup tableGroup = tableGroupRequest.toTableGroup(orderTables);
		return tableGroupRepository.save(tableGroup);
	}

	private void validateOrderTableIsUseOrIsGrouped(List<OrderTable> orderTables) {
		if (isUseOrIsGrouped(orderTables)) {
			throw new IllegalArgumentException("이미 사용중이거나 그룹화된 테이블은 그룹생성 할 수 없습니다");
		}
	}

	private boolean isUseOrIsGrouped(List<OrderTable> orderTables) {
		return orderTables.stream()
			.anyMatch(orderTable -> orderTable.isUseOrIsGrouped());
	}

	private List<Long> extractOrderTableIds(TableGroupRequest tableGroupRequest) {
		return tableGroupRequest.getOrderTableRequests()
			.stream()
			.map(orderTableRequest -> orderTableRequest.getId())
			.collect(Collectors.toList());
	}

	private void validateOrderTableSizeLessThanTwo(TableGroupRequest tableGroupRequest) {
		List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTableRequests();
		if (IsSizeLessThanTwo(orderTableRequests)) {
			throw new IllegalArgumentException("2개 이상의 테이블만 그룹생성이 가능합니다");
		}
	}

	private boolean IsSizeLessThanTwo(List<OrderTableRequest> orderTableRequests) {
		return CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2;
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = findTableGroupById(tableGroupId);
		validateOrderIsCompletion(tableGroup);
		ungroupOrderTable(tableGroup);
	}

	private TableGroup findTableGroupById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId)
			.orElseThrow(() -> new IllegalArgumentException("테이블그룹이 존재하지 않습니다."));
	}

	private void ungroupOrderTable(TableGroup tableGroup) {
		tableGroup.getOrderTables()
			.forEach(OrderTable::ungroup);
	}

	private void validateOrderIsCompletion(TableGroup tableGroup) {
		if (!isOrderCompletion(tableGroup)) {
			throw new IllegalArgumentException("아직 테이블의 주문이 계산완료되지 않았습니다");
		}
	}

	private boolean isOrderCompletion(TableGroup tableGroup) {
		return tableGroup.getOrderTables().stream()
			.allMatch(OrderTable::isOrderCompletion);
	}
}
