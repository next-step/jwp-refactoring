package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;


    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = findOrderTablesByIds(request);
        TableGroup saveTableGroup = tableGroupRepository.save(TableGroup.from(savedOrderTables));
        return TableGroupResponse.from(saveTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderValidator.checkUnGroupable(orderTables.getOrderTableIds());
        orderTables.unGroup();
    }

    private List<OrderTable> findOrderTablesByIds(TableGroupRequest request) {
        return request.getOrderTableIds()
                .stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ORDER_TABLE_NOT_FOUND));
    }
}
