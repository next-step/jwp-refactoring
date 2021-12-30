package kitchenpos.order.application;


import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(orderTables), LocalDateTime.now()));
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new InputTableDataException(InputTableDataErrorCode.THE_TABLE_GROUP_CAN_NOT_FIND));
        tableGroup.cancleGroup();
    }
}
