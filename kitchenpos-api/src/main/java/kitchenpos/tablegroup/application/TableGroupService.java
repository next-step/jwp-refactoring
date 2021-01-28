package kitchenpos.tablegroup.application;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private static final int TABLE_GROUP_MINIMUM_COUNT = 2;

	private final OrderService orderService;
	private final OrderTableService orderTableService;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(final OrderService orderService, final OrderTableService orderTableService,
		final TableGroupRepository tableGroupRepository) {
		this.orderService = orderService;
		this.orderTableService = orderTableService;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest request) {
		final OrderTables orderTables = OrderTables.of(orderTableService.findAllById(request.getOrderTables()));
		validateOrderTables(orderTables, request.getOrderTables());

		final TableGroup persistGroup = tableGroupRepository.save(TableGroup.of());
		orderTables.createGroup(persistGroup);

		return TableGroupResponse.of(persistGroup, orderTables);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final TableGroup persistTableGroup = findById(tableGroupId);
		final OrderTables orderTables = OrderTables.of(orderTableService.findAllByTableGroup(persistTableGroup));
		final Orders order = orderService.findByOrderTable(orderTables.getOrderTables().get(0));
		order.validateStatus();
		orderTables.clearTableGroup();
		tableGroupRepository.delete(persistTableGroup);
	}

	private void validateOrderTables(OrderTables orderTables, List<Long> orderTableIds) {
		if (orderTables.size() < TABLE_GROUP_MINIMUM_COUNT) {
			throw new IllegalArgumentException("단체 테이블은 테이블 수가 2개 이상이여야 합니다.");
		}
		if (orderTables.size() != orderTableIds.size()) {
			throw new EntityNotFoundException("요청하신 테이블을 찾을 수 없습니다.");
		}
		orderTables.validateIsGroup();
		orderTables.validateNotEmpty();
	}

	private TableGroup findById(Long tableGroupId) {
		return tableGroupRepository.findById(tableGroupId).orElseThrow(EntityNotFoundException::new);
	}
}


