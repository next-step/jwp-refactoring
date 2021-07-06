package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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
	public TableGroup create(final TableGroup tableGroup) {
		final List<OrderTable> orderTables = tableGroup.getOrderTables();

		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException("주문 테이블이이 둘 이상이어야 단체지정을 할 수 있습니다.");
		}

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

		if (orderTables.size() != savedOrderTables.size()) {
			throw new IllegalArgumentException("주문 테이블과 저장된 주문 테이블의 갯수가 다릅니다.");
		}

		for (final OrderTable savedOrderTable : savedOrderTables) {
			if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
				throw new IllegalArgumentException("주문테이블이 단체지정이 되어있거나, 비어있지 않은 테이블입니다.");
			}
		}

		tableGroup.setCreatedDate(LocalDateTime.now());

		final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

		final Long tableGroupId = savedTableGroup.getId();
		for (final OrderTable savedOrderTable : savedOrderTables) {
			savedOrderTable.setTableGroup(savedTableGroup);
			savedOrderTable.setEmpty(false);
			orderTableDao.save(savedOrderTable);
		}
		savedTableGroup.setOrderTables(savedOrderTables);

		return savedTableGroup;
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
			orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException("주문 상태가 완료되어야 단체지정이 해제가능합니다.");
		}

		for (final OrderTable orderTable : orderTables) {
			orderTable.setTableGroup(null);
			orderTableDao.save(orderTable);
		}
	}
}
