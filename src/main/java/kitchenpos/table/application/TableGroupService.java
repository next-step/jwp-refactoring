package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@Transactional
@Service
public class TableGroupService {
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;
	private final TableGroupValidator tableGroupValidator;

	public TableGroupService(final OrderTableRepository orderTableRepository,
		final TableGroupRepository tableGroupRepository,
		TableGroupValidator tableGroupValidator) {
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
		this.tableGroupValidator = tableGroupValidator;
	}

	public TableGroupResponse create(final TableGroupRequest request) {
		tableGroupValidator.validateCreate(request.getOrderTableIds());
		TableGroup tableGroup = TableGroup.create();
		tableGroup = tableGroupRepository.save(tableGroup);
		List<OrderTable> tables = getOrderTables(request.getOrderTableIds());
		return new TableGroupResponse(tableGroup, tables);
	}

	private List<OrderTable> getOrderTables(List<Long> tableIds) {
		return orderTableRepository.findAllById(tableIds);
	}

	public void ungroup(final Long tableGroupId) {
		List<OrderTable> orderTableList = orderTableRepository.findByTableGroupId(tableGroupId);
		tableGroupValidator.validateUnGroup(orderTableList);
		for (OrderTable orderTable : orderTableList) {
			orderTable.unGroup();
		}
	}

}
