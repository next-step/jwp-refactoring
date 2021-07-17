package kitchenpos.tableGroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableRequest;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
		final List<OrderTable> orderTables = tableGroupRequest.getOrderTables().stream()
				.map(orderTableRequest -> orderTableRepository
					.findByTableGroupId(orderTableRequest.getId())
					.orElseThrow(IllegalAccessError::new))
                .collect(Collectors.toList());

		TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));

		return TableGroupResponse.of(tableGroup);
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
