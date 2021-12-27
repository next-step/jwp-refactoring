package kitchenpos.tablegroup.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.exception.TableGroupException;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private final TableGroupRepository tableGroupRepository;
	private final TableGroupValidator tableGroupValidator;

	public TableGroupService(final TableGroupRepository tableGroupRepository,
		final TableGroupValidator tableGroupValidator) {
		this.tableGroupRepository = tableGroupRepository;
		this.tableGroupValidator = tableGroupValidator;
	}

	@Transactional
	public TableGroup create(final TableGroupRequest tableGroupRequest) {
		OrderTables orderTables = tableGroupValidator.findValidatedOrderTables(tableGroupRequest.getOrderTables());
		TableGroup tableGroup = tableGroupRepository.save(TableGroup.from());
		orderTables.changeTableGroup(tableGroup.getId());
		return tableGroup;
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup tableGroup = tableGroupFindById(tableGroupId);
		OrderTables orderTables = tableGroupValidator.findCompletionOrderTables(tableGroup.getId());
		orderTables.unGroupOrderTables();
		tableGroupRepository.delete(tableGroup);
	}

	private TableGroup tableGroupFindById(Long id) {
		return tableGroupRepository.findById(id)
			.orElseThrow(() -> {
				throw new TableGroupException(ErrorCode.TABLE_GROUP_IS_NULL);
			});
	}
}
