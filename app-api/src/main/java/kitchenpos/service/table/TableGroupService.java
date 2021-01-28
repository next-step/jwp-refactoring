package kitchenpos.service.table;

import kitchenpos.domain.order.OrderCollection;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;

import static kitchenpos.domain.order.OrderCollection.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableService tableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
    }

    public TableGroupResponse applyGroup(TableGroupRequest tableGroupRequest) {
        List<Long> ids = tableGroupRequest.getTableIds();
        List<OrderTable> orderTables = tableService.findOrderTables(ids);
        OrderTableGroup orderTableGroup = tableGroupRepository.save(new OrderTableGroup());
        orderTableGroup.applyGroup(ids.size(), orderTables);
        return TableGroupResponse.of(orderTableGroup, orderTables);
    }

    public void applyUnGroup(Long id) {
        OrderTableGroup orderTableGroup = tableGroupRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        List<OrderTable> orderTables = tableService.findOrderTablesByOrderTableGroup(orderTableGroup);
        new OrderCollection(toOrders(orderTables)).checkOrderStatus();
        orderTableGroup.applyUnGroup(orderTables);
    }
}
