package kitchenpos.order.application;


import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;
    private final OrderTableValidator orderTableValidator;


    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository,
                             final OrderRepository orderRepository, final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(orderTables)));
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new InputTableDataException(InputTableDataErrorCode.THE_TABLE_GROUP_CAN_NOT_FIND));

        List<OrderTable> orderTableBasket = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        OrderTables orderTables = new OrderTables(orderTableBasket);
        List<Long> orderTableIds = orderTables.getOrderTableIds();

        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        orderTableValidator.cancelTableGroup(orders);
        tableGroup.cancleGroup();

    }
}
