package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;

@Service
public class TableGroupService {

	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
		final TableGroupRepository tableGroupRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroup create(final TableGroup tableGroup) {
		final List<OrderTable> orderTables = tableGroup.getOrderTables();

		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException();
		}

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

		if (orderTables.size() != savedOrderTables.size()) {
			throw new IllegalArgumentException();
		}

		for (final OrderTable savedOrderTable : savedOrderTables) {
			if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
				throw new IllegalArgumentException();
			}
		}

		final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

		for (final OrderTable savedOrderTable : savedOrderTables) {
			savedOrderTable.setTableGroup(savedTableGroup);
			savedOrderTable.setEmpty(false);
			orderTableRepository.save(savedOrderTable);
		}
		savedTableGroup.setOrderTables(savedOrderTables);

		return savedTableGroup;
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup_Id(tableGroupId);

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		if (orderRepository.existsByOrderTable_IdInAndOrderStatusIn(
			orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new IllegalArgumentException();
		}

		for (final OrderTable orderTable : orderTables) {
			orderTable.setTableGroup(null);
			orderTableRepository.save(orderTable);
		}
	}
}
