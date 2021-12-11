package kitchenpos.application.table;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderService orderService, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderService = orderService;
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

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(savedOrderTables));

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
        for (final OrderTable orderTable : savedOrderTables) {
            checkHasTableGroup(orderTable);
            checkNotEmptyTable(orderTable);
        }
    }

    private void checkHasTableGroup(final OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotEmptyTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException();
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
        if (orderService.isExistNotCompletionOrder(orderTableIds)) {
            throw new IllegalArgumentException();
        }
    }
}
