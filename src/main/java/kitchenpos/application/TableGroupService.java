package kitchenpos.application;

import kitchenpos.common.NotFoundException;
import kitchenpos.common.TableGroupValidationException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private static final int TABLE_GROUP_MIN = 2;
	static final String MSG_TABLE_ID_MUST_GREATER = String.format("TableIds'size must be greater than %d", TABLE_GROUP_MIN);
	static final String MSG_ORDER_TABLE_EMPTY = "OrderTable must be empty";
	static final String MSG_ORDER_TABLE_ALREADY_GROUP = "OrderTable already has TableGroup";
	static final String MSG_ORDER_TABLE_ONGOING = "OrderTable's OrderStatus is ongoing";
	static final String CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by tableId";
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
		if (request.getTableIds().size() < TABLE_GROUP_MIN) {
			throw new TableGroupValidationException(MSG_TABLE_ID_MUST_GREATER);
		}

		final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(request.getTableIds());
		if (request.getTableIds().size() != savedOrderTables.size()) {
			throw new NotFoundException(CANNOT_FIND_ORDER_TABLE);
		}

		for (final OrderTable savedOrderTable : savedOrderTables) {
			if (Objects.nonNull(savedOrderTable.getTableGroup())) {
				throw new TableGroupValidationException(MSG_ORDER_TABLE_ALREADY_GROUP);
			}
			if (!savedOrderTable.isEmpty()) {
				throw new TableGroupValidationException(MSG_ORDER_TABLE_EMPTY);
			}
		}

		final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup());
		for (final OrderTable savedOrderTable : savedOrderTables) {
			// TODO: 2021-01-18 하나의 메소드로 묶기
			savedOrderTable.putIntoGroup(savedTableGroup);
			savedOrderTable.changeEmpty(false);
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
			throw new NotFoundException(MSG_ORDER_TABLE_ONGOING);
		}

		for (final OrderTable orderTable : orderTables) {
			orderTable.ungroup();
			orderTableDao.save(orderTable);
		}
		// TODO: 2021-01-15 delete TableGroup
	}
}
