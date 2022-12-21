package kitchenpos.order.application;

import static kitchenpos.order.ui.request.TableGroupRequest.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.ui.request.TableGroupRequest;
import kitchenpos.order.ui.response.TableGroupResponse;

@Service
public class TableGroupService {
	private final TableGroupRepository tableGroupRepository;
	private final TableService tableService;
	private final OrderService orderService;

	public TableGroupService(
		TableGroupRepository tableGroupRepository,
		TableService tableService,
		OrderService orderService
	) {
		this.tableGroupRepository = tableGroupRepository;
		this.tableService = tableService;
		this.orderService = orderService;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest request) {
		final List<OrderTableIdRequest> orderTables = request.getOrderTables();

		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException();
		}

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTableIdRequest::getId)
			.collect(Collectors.toList());

		final List<OrderTable> savedOrderTables = tableService.findAllByIdIn(orderTableIds);

		if (orderTables.size() != savedOrderTables.size()) {
			throw new IllegalArgumentException();
		}

		for (final OrderTable savedOrderTable : savedOrderTables) {
			if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.tableGroup())) {
				throw new IllegalArgumentException();
			}
		}

		final TableGroup savedTableGroup = tableGroupRepository.save(request.toEntity());

		return TableGroupResponse.from(savedTableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		// final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
		//
		// final List<Long> orderTableIds = orderTables.stream()
		// 	.map(OrderTable::getId)
		// 	.collect(Collectors.toList());
		//
		// if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
		// 	orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
		// 	throw new IllegalArgumentException();
		// }
		//
		// for (final OrderTable orderTable : orderTables) {
		// 	// orderTable.setTableGroupId(null);
		// 	orderTableDao.save(orderTable);
		// }
	}
}
