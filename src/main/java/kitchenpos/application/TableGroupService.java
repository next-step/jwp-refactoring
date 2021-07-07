package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
	private final OrderDao orderDao;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public TableGroupService(OrderDao orderDao, OrderTableRepository orderTableRepository,
		TableGroupRepository tableGroupRepository) {
		this.orderDao = orderDao;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
		List<OrderTable> orderTables = orderTableIds.stream().map(this::findOrderTable).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException("주문 테이블이이 둘 이상이어야 단체지정을 할 수 있습니다.");
		}

		TableGroup tableGroup = new TableGroup(LocalDateTime.now());
		for (final OrderTable orderTable : orderTables) {
			tableGroup.addOrderTable(orderTable);
		}
		return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
	}

	private OrderTable findOrderTable(Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 주문 테이블을 찾을 수 없습니다."));
	}

	@Transactional
	public void ungroup(final Long tableGroupId) {
		final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

		final List<Long> orderTableIds = orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
			orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException("주문 상태가 완료되어야 단체지정이 해제가능합니다.");
		}

		for (final OrderTable orderTable : orderTables) {
			orderTable.setTableGroup(null);
			orderTableRepository.save(orderTable);
		}
	}
}
