package kitchenpos.application;

import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrdersRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrdersRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroup) {
        
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                                                    .map(OrderTableDto::getId)
                                                    .collect(Collectors.toList());


        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validationOfCreate(tableGroup.getOrderTables(), savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(LocalDateTime.now(), savedOrderTables));

        updateOrderTable(savedOrderTables, savedTableGroup);

        savedTableGroup.changeOrderTables(savedOrderTables);

        return TableGroupDto.of(savedTableGroup);
    }

    private void updateOrderTable(final List<OrderTable> savedOrderTables, final TableGroup savedTableGroup) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.changeTableGroup(savedTableGroup);
            savedOrderTable.changeEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
    }

    private void validationOfCreate(final List<OrderTableDto> orderTables, final List<OrderTable> savedOrderTables) {
        checkOrderTableSize(orderTables);
        checkAllExistOfOrderTables(orderTables, savedOrderTables);
        checkHasTableGroupOfSavedOrderTable(savedOrderTables);
    }

    private void checkHasTableGroupOfSavedOrderTable(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.hasTableGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkAllExistOfOrderTables(final List<OrderTableDto> orderTables, final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkOrderTableSize(final List<OrderTableDto> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());

        validationOfUpgroup(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
            orderTableRepository.save(orderTable);
        }
    }

    private void validationOfUpgroup(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
