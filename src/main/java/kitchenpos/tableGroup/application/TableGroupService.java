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
/*
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTableRequest> savedOrderTables = orderTableRepository.findAllByIdIn(orderTables);

        if (orderTableRequests.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTableRequest savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        tableGroupRequest.setCreatedDate(LocalDateTime.now());

        final kitchenpos.tableGroup.dto.TableGroupRequest savedTableGroup = this.tableGroupRepository.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTableRequest savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);
*/
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
/*
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTableRepository.save(orderTable);
        }
*/
	}
}
