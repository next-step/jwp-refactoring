package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.table.domain.GroupTablesValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.UnGroupTablesValidator;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

	private final TableGroupRepository tableGroupRepository;
	private final OrderTableRepository orderTableRepository;
	private final UnGroupTablesValidator unGroupTablesValidator;
	private final GroupTablesValidator groupTablesValidator;

	public TableGroupService(TableGroupRepository tableGroupRepository,
							 OrderTableRepository orderTableRepository,
							 UnGroupTablesValidator unGroupTablesValidator,
							 GroupTablesValidator groupTablesValidator) {
		this.tableGroupRepository = tableGroupRepository;
		this.orderTableRepository = orderTableRepository;
		this.unGroupTablesValidator = unGroupTablesValidator;
		this.groupTablesValidator = groupTablesValidator;
	}

	@Transactional
	public TableGroup group(List<Long> orderTableId) {
		List<OrderTable> orderTable = findAllOrderTables(orderTableId);

		TableGroup tableGroup = TableGroup.group(orderTable, groupTablesValidator);

		return tableGroupRepository.save(tableGroup);
	}

	@Transactional
	public void ungroup(Long tableGroupId) {
		TableGroup savedTableGroup = findById(tableGroupId);
		unGroupTablesValidator.validate(savedTableGroup);
		savedTableGroup.ungroup();
	}

	private TableGroup findById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId)
								   .orElseThrow(EntityNotFoundException::new);
	}

	private List<OrderTable> findAllOrderTables(List<Long> orderTableId) {
		List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableId);
		validateExistsAll(orderTableId, orderTables);
		return orderTables;
	}

	private void validateExistsAll(List<Long> orderTableId, List<OrderTable> orderTable) {
		if (orderTableId.size() != orderTable.size()) {
			throw new EntityNotFoundException();
		}
	}
}
