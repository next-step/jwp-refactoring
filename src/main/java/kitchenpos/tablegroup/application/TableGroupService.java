package kitchenpos.tablegroup.application;

import kitchenpos.orders.application.OrderService;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.OrderTables;
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
	private final OrderTableService orderTableService;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(OrderTableService orderTableService,
		TableGroupRepository tableGroupRepository) {
		this.orderTableService = orderTableService;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroup create(final TableGroupRequest tableGroupRequest) {
		validateOrderTableSizeLessThanTwo(tableGroupRequest);

		OrderTables orderTables = orderTableService.findOrderTableByIdIn(tableGroupRequest.extractOrderTableIds());
		orderTables.validateOrderTableIsUseOrIsGrouped();

		TableGroup tableGroup = tableGroupRequest.toTableGroup(orderTables);
		return tableGroupRepository.save(tableGroup);
	}

	private void validateOrderTableSizeLessThanTwo(TableGroupRequest tableGroupRequest) {
		if (IsSizeLessThanTwo(tableGroupRequest.getOrderTableRequests())) {
			throw new IllegalArgumentException("2개 이상의 테이블만 그룹생성이 가능합니다");
		}
	}

	private boolean IsSizeLessThanTwo(List<OrderTableRequest> orderTableRequests) {
		return CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2;
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = findTableGroupById(tableGroupId);
		tableGroup.ungrouped();
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
}
