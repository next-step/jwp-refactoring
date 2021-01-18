package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest_Create;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;
	private final TableGroupDao tableGroupDao;

	public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
		this.tableGroupDao = tableGroupDao;
	}

	@Transactional
	public TableGroupResponse create(TableGroupRequest_Create request) {
		if (CollectionUtils.isEmpty(request.getTableIds()) || request.getTableIds().size() < 2) {
			throw new IllegalArgumentException();
		}

		final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(request.getTableIds());
		if (request.getTableIds().size() != savedOrderTables.size()) {
			throw new IllegalArgumentException();
		}

		for (final OrderTable savedOrderTable : savedOrderTables) {
			if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
				throw new IllegalArgumentException();
			}
		}

		final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup());
		for (final OrderTable savedOrderTable : savedOrderTables) {
			savedOrderTable.setTableGroup(savedTableGroup);
			savedOrderTable.setEmpty(false); // TODO: 2021-01-15 이미 Empty 되어있는 테이블만 가능하므로 제거할 것
			orderTableDao.save(savedOrderTable);
		}
		savedTableGroup.setOrderTables(savedOrderTables);

		return TableGroupResponse.of(savedTableGroup);
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
		// TODO: 2021-01-15 delete TableGroup
	}
}
