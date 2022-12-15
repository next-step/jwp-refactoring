package kitchenpos.table.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final OrderService orderService;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableService tableService, OrderService orderService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTableRequests()
                .stream()
                .map(tableService::findOrderTableById)
                .collect(Collectors.toList());

        TableGroup saved = tableGroupRepository.save(TableGroup.of(OrderTables.of(orderTables)));
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        boolean isAllFinished = tableGroup.getOrderTables()
                .getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .allMatch(orderService::isAllOrderFinished);
        if(!isAllFinished){
            throw new IllegalArgumentException(ErrorMessage.CANNOT_UNGROUP_WHEN_ORDER_NOT_COMPLETED);
        }
        tableGroup.unGroup();
    }
    private TableGroup findTableGroupById(Long tableGroupId){
        return tableGroupRepository.findById(tableGroupId).orElseThrow(
                ()-> new EntityNotFoundException("단체지정", tableGroupId)
        );
    }

}
