package kichenpos.table.application;

import kichenpos.table.domain.OrderTable;
import kichenpos.table.domain.OrderTableRepository;
import kichenpos.table.domain.TableGroup;
import kichenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroup.getOrderTableIds());
        tableGroup.changeOrderTables(savedOrderTables);
        tableGroup.create(LocalDateTime.now());

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정을 찾을 수 없습니다."));
        orderTableValidator.validateUngroup(tableGroup.getOrderTableIds());

        tableGroup.ungroupOrderTables();
    }
}
