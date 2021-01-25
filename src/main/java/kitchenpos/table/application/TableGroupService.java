package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupReponse;
import kitchenpos.table.dto.TableGroupRequest;

@Service
public class TableGroupService {
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;
	private final TableGroupDao tableGroupDao;

	public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
		final TableGroupDao tableGroupDao) {
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
		this.tableGroupDao = tableGroupDao;
	}

	@Transactional
	public TableGroupReponse create(final TableGroupRequest request) {
		final List<OrderTableRequest> orderTables = request.getOrderTables();

		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException();
		}

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTableRequest::getId)
			.collect(Collectors.toList());

		final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

		if (orderTables.size() != savedOrderTables.size()) {
			throw new IllegalArgumentException();
		}

		for (final OrderTable savedOrderTable : savedOrderTables) {
			if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
				throw new IllegalArgumentException();
			}
		}

		final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup.Builder()
			.createdDate(LocalDateTime.now())
			.build());

		final Long tableGroupId = savedTableGroup.getId();
		for (final OrderTable savedOrderTable : savedOrderTables) {
			savedOrderTable.setTableGroup(savedTableGroup);
			savedOrderTable.setEmpty(false);
			orderTableDao.save(savedOrderTable);
		}
		savedTableGroup.setOrderTables(savedOrderTables);

		return TableGroupReponse.from(savedTableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
			orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new IllegalArgumentException();
		}

		for (final OrderTable orderTable : orderTables) {
			orderTable.setTableGroup(null);
			orderTableDao.save(orderTable);
		}
	}
}
