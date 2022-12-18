package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.ui.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

	private final TableGroupRepository tableGroupRepository;
	private final TableService tableService;

	public TableGroupService(TableGroupRepository tableGroupRepository,
							 TableService tableService) {
		this.tableGroupRepository = tableGroupRepository;
		this.tableService = tableService;
	}

	@Transactional
	public TableGroupResponse create(List<Long> orderTableId) {
		return new TableGroupResponse(save(orderTableId));
	}

	@Transactional
	public TableGroup save(List<Long> orderTableId) {
		List<OrderTable> orderTable = tableService.findAllById(orderTableId);
		validateExistsAll(orderTableId, orderTable);

		TableGroup tableGroup = TableGroup.create(orderTable);

		return tableGroupRepository.save(tableGroup);
	}

	private void validateExistsAll(List<Long> orderTableId, List<OrderTable> orderTable) {
		if (orderTableId.size() != orderTable.size()) {
			throw new EntityNotFoundException();
		}
	}

	@Transactional
	public void ungroup(Long tableGroupId) {
		TableGroup savedTableGroup = findById(tableGroupId);
		savedTableGroup.ungroup();
	}

	private TableGroup findById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId)
								   .orElseThrow(EntityNotFoundException::new);
	}
}
