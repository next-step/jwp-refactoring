package kitchenpos.table.application;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotCreateTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final TableGroupValidator tableGroupValidator,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateRequest(tableGroupRequest);

        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds()));
        TableGroup tableGroup = TableGroup.create(tableGroupValidator, tableGroupRequest.getOrderTableIds(), orderTables);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroup(tableGroupId));
        orderTables.unGroup(tableGroupValidator);
    }

    private void validateRequest(TableGroupRequest tableGroupRequest) {
        final List<Long> orderTables = tableGroupRequest.getOrderTableIds();
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new NotCreateTableGroupException("주문 테이블 개수가 부족합니다.");
        }

    }

}
