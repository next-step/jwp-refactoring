package kitchenpos.table.table.appliation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.table.domain.OrderTableRepository;
import kitchenpos.table.table.domain.TableGroup;
import kitchenpos.table.table.domain.TableGroupRepository;
import kitchenpos.table.table.ui.request.TableGroupRequest;
import kitchenpos.table.table.ui.response.TableGroupResponse;

@Service
public class TableGroupService {
	private final TableGroupRepository tableGroupRepository;
	private final OrderTableRepository orderTableRepository;
	private final TableValidator tableValidator;

	public TableGroupService(
		final TableGroupRepository tableGroupRepository,
		final OrderTableRepository orderTableRepository,
		final TableValidator tableValidator
	) {
		this.tableGroupRepository = tableGroupRepository;
		this.orderTableRepository = orderTableRepository;
		this.tableValidator = tableValidator;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest request) {
		final TableGroup tableGroup = tableGroupRepository.save(newTableGroup(request));
		return TableGroupResponse.from(tableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup tableGroup = tableGroup(tableGroupId);
		tableValidator.validateUngroup(tableGroup);
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

	private TableGroup tableGroup(Long tableGroupId) {
		return tableGroupRepository.tableGroup(tableGroupId);
	}
}
