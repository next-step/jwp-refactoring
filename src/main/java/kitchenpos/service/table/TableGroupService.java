package kitchenpos.service.table;

import kitchenpos.domain.order.OrderCollection;
import kitchenpos.domain.table.OrderTableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.service.order.OrderService;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final OrderService orderService;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableService tableService, final OrderService orderService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    public TableGroupResponse applyGroup(TableGroupRequest tableGroupRequest) {
        List<Long> ids = tableGroupRequest.getTableIds();
        OrderTableGroup orderTableGroup = new OrderTableGroup(tableService.findOrderTables(ids));
        orderTableGroup.applyGroup(ids.size());
        return TableGroupResponse.of(tableGroupRepository.save(orderTableGroup));
    }

    public void applyUnGroup(Long id) {
        OrderTableGroup orderTableGroup = tableGroupRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        new OrderCollection(orderService.findOrderByTableIn(orderTableGroup.getOrderTables())).checkOrderStatus();
        orderTableGroup.applyUnGroup();
    }
}
