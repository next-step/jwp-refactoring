package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
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
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.NotFoundException;

@Service
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
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

		if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
			throw new IllegalArgumentException();
		}

		final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

		if (orderTableIds.size() != savedOrderTables.size()) {
			throw new IllegalArgumentException();
		}
		final TableGroup savedTableGroup = tableGroupDao.save(TableGroup.create(savedOrderTables));
		return TableGroupResponse.of(savedTableGroup);
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
			.orElseThrow(() -> new NotFoundException("단체 테이블 정보를 찾을 수 없습니다."));
		final List<OrderTable> orderTables = tableGroup.getOrderTables();

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		//memo [2021-01-5 20:43] TableService와 동일한 코드가 있는데 어떻게 빼면 좋을까
		if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
			orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		tableGroup.ungroup();

	}
}
