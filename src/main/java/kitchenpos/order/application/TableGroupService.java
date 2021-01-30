package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;

@Service
public class TableGroupService {
	private final TableGroupRepository tableGroupRepository;
	private final TableService tableService;

	public TableGroupService(TableGroupRepository tableGroupRepository, TableService tableService) {
		this.tableGroupRepository = tableGroupRepository;
		this.tableService = tableService;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
		List<OrderTable> orderTables = tableService.findAllOrderTablesByIds(orderTableIds);
		TableGroup tableGroup = TableGroup.builder().orderTables(orderTables).build();

		return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);

		tableGroupRepository.deleteById(tableGroupId);
	}
}
