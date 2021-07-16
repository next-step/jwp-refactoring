package kitchenpos.application.table;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_TABLE_GROUP;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.exception.KitchenposException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = getOrderTables(request);
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup(savedOrderTables)));
    }

    private List<OrderTable> getOrderTables(TableGroupRequest request) {
        return request.getOrderTables()
                      .stream()
                      .map(this::findOrderTableById)
                      .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(OrderTableIdRequest orderTableIdRequest) {
        return orderTableRepository.findById(orderTableIdRequest.getId())
                                   .orElseThrow(() -> new KitchenposException(
                                       NOT_FOUND_ORDER_TABLE));
    }

    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = findTableGroupById(tableGroupId).getOrderTables();
        orderValidator.checkNotCompletionOrders(orderTables.getOrderTableIds());
        orderTables.ungroup();
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                                   .orElseThrow(() -> new KitchenposException(
                                       NOT_FOUND_TABLE_GROUP));
    }
}
