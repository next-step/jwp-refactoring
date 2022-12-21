package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.tablegroup.port.TableGroupPort;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final TableGroupPort tableGroupPort;
    private final TableGroupValidator tableGroupValidator;
    private final OrderTablePort orderTablePort;

    public TableGroupService(TableGroupPort tableGroupPort, TableGroupValidator tableGroupValidator, OrderTablePort orderTablePort) {
        this.tableGroupPort = tableGroupPort;
        this.tableGroupValidator = tableGroupValidator;
        this.orderTablePort = orderTablePort;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        tableGroupValidator.makeTableGroup(request);
        List<OrderTable> orderTables = orderTablePort.findAllByTableGroupIdIn(request.getOrderTableIds());

        TableGroup savedTableGroup = tableGroupPort.save(new TableGroup());

        return TableGroupResponse.from(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupPort.findById(tableGroupId);
        tableGroupValidator.ungroup(tableGroup);

        tableGroup.ungroup();
    }
}
