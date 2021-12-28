package kitchenpos.table.application;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import kitchenpos.common.*;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.*;
import kitchenpos.table.repository.*;

@Service
@Transactional
public class TableGroupService {
    private static final String TABLE_GROUP = "테이블 그룹";
    private static final String INSUFFICIENT_ORDER_TABLE_EXCEPTION_STATEMENT = "주문 테이블이 부족할 수 있습니다.";

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse saveTableGroup(TableGroupRequest tableGroupRequest) {
        TableGroup saveTableGroup = new TableGroup();
        List<OrderTable> orderTables = orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds());

        validateOrderTable(tableGroupRequest, orderTables);
        orderTables.stream()
            .forEach(saveTableGroup::addOrderTable);

        return TableGroupResponse.of(tableGroupRepository.save(saveTableGroup));
    }

    private void validateOrderTable(TableGroupRequest tableGroupRequest, List<OrderTable> orderTables) {
        if (tableGroupRequest.getOrderTableIds().size() != orderTables.size()) {
            throw new IllegalArgumentException(INSUFFICIENT_ORDER_TABLE_EXCEPTION_STATEMENT);
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException(TABLE_GROUP));
        savedTableGroup.ungroup();
        tableGroupRepository.delete(savedTableGroup);
    }
}
