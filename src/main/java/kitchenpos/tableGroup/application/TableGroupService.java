package kitchenpos.tableGroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderDao, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
		final List<Long> orderTableIds = tableGroupRequest.getOrderTables().stream()
			.map(OrderTableRequest::getId)
			.collect(Collectors.toList());

		List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

		if (orderTableIds.size() != orderTables.size()) {
			throw new IllegalArgumentException();
		}

		TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
		savedTableGroup.addOrderTables(orderTables);

		return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

		for (final OrderTable orderTable : orderTables) {
			orderTable.unGroup();
			orderTableRepository.save(orderTable);
		}
	}
}
