package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public TableGroupResponse create(final TableGroupRequest request) {
		List<OrderTable> savedOrderTables = orderTableService
			  .findAllByOrderTableIds(request.getOrderTableIds());

		OrderTables orderTables = new OrderTables(savedOrderTables, request.getRequestSize());

		TableGroup savedTableGroup = tableGroupRepository.save(request.toEntity(orderTables));
		return TableGroupResponse.of(savedTableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = findById(tableGroupId);
		tableGroup.unTableGroup();
		tableGroupRepository.deleteById(tableGroup.getId());
	}

	private TableGroup findById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId)
			  .orElseThrow(IllegalArgumentException::new);
	}
}
