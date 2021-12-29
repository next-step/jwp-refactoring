package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {

	private final OrderTableService orderTableService;
	private final TableGroupRepository tableGroupRepository;
	private final TableGroupValidator tableGroupValidator;

	public TableGroupService(OrderTableService orderTableService,
		TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
		this.orderTableService = orderTableService;
		this.tableGroupRepository = tableGroupRepository;
		this.tableGroupValidator = tableGroupValidator;
	}

	@Transactional
	public TableGroup create(final TableGroupRequest tableGroupRequest) {
		List<Long> orderTableIds = tableGroupRequest.extractOrderTableIds();
		List<OrderTable> orderTables = orderTableService.findOrderTableByIdIn(orderTableIds);
		tableGroupValidator.validateCreate(orderTables);
		TableGroup save = tableGroupRepository.save(tableGroupRequest.toTableGroup());
		orderTablesGrouped(orderTables, save);
		return save;
	}

	private void orderTablesGrouped(List<OrderTable> orderTables, TableGroup save) {
		orderTables.forEach(orderTable -> orderTable.toGroup(save.getId()));
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		validateIsExistedTableGroup(tableGroupId);
		List<OrderTable> orderTables = orderTableService.findOrderTablesByTableGroupId(tableGroupId);
		tableGroupValidator.validateUngroup(orderTables);
		orderTablesUngrouped(orderTables);
	}

	private void validateIsExistedTableGroup(Long tableGroupId) {
		findTableGroupById(tableGroupId);
	}

	private void orderTablesUngrouped(List<OrderTable> orderTables) {
		orderTables.forEach(OrderTable::ungroup);
	}

	private TableGroup findTableGroupById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId)
			.orElseThrow(() -> new IllegalArgumentException("테이블그룹이 존재하지 않습니다."));
	}

}
