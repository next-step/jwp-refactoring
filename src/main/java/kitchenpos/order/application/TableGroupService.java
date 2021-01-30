package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;

@Service
public class TableGroupService {
	private final TableGroupRepository tableGroupRepository;
	private final TableService tableService;
	private final OrderService orderService;

	public TableGroupService(TableGroupRepository tableGroupRepository, TableService tableService,
		OrderService orderService) {
		this.tableGroupRepository = tableGroupRepository;
		this.tableService = tableService;
		this.orderService = orderService;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
		List<OrderTable> orderTables = tableService.findAllOrderTablesByIds(tableGroupRequest.getOrderTableIds());
		tableGroup.group(orderTables);

		return TableGroupResponse.of(tableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);

		Orders orders = new Orders(orderService.findAllOrderByOrderTableIds(tableGroup.getOrderTableIds()));
		orders.validateStatusNotCompletion();

		tableGroup.ungroup();
		tableGroupRepository.deleteById(tableGroupId);
	}
}
