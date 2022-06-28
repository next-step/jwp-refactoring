package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableValidator;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroup create(final List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableIds.stream()
                .map(orderTableValidator::findExistsOrderTableById)
                .collect(toList());

        return tableGroupRepository.save(TableGroup.group(savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findByIdWithOrderTable(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGroup.getOrderTables().getOrderTables().stream()
                .map(OrderTable::getId)
                .forEach(orderTableValidator::checkOrderStatus);

        tableGroup.ungroup();
    }
}
