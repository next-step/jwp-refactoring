package kitchenpos.tablegroup.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.ordertable.application.OrderTableQueryService;
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

	private final OrderTableQueryService orderTableQueryService;
	private final OrderTableService orderTableService;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(
		  OrderTableQueryService orderTableQueryService,
		  OrderTableService orderTableService,
		  TableGroupRepository tableGroupRepository) {
		this.orderTableQueryService = orderTableQueryService;
		this.orderTableService = orderTableService;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest request) {
		List<OrderTable> savedOrderTables = orderTableQueryService
			  .findAllByOrderTableIds(request.getOrderTableIds());

		OrderTables orderTables = new OrderTables(savedOrderTables, request.getRequestSize());
		TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.newInstance());
		orderTables.group(savedTableGroup);

		return TableGroupResponse.of(orderTables);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = findById(tableGroupId);
		orderTableService.unGroup(tableGroup);
		tableGroupRepository.deleteById(tableGroup.getId());
	}

	private TableGroup findById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId)
			  .orElseThrow(EntityNotFoundException::new);
	}
}
