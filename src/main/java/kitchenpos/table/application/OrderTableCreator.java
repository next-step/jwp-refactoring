package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableCreator {

    private final TableGroupRepository tableGroupRepository;

    public OrderTableCreator(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    private TableGroup getTableGroupById(OrderTableRequest orderTableRequest) {
        TableGroup tableGroup = null;
        Long tableGroupId = orderTableRequest.getTableGroupId();

        if (tableGroupId != null) {
            tableGroup = tableGroupRepository.findById(tableGroupId).orElse(null);
        }
        return tableGroup;
    }

    public OrderTable toOrderTable(OrderTableRequest orderTableRequest) {
        TableGroup tableGroup = getTableGroupById(orderTableRequest);

//        return OrderTable.builder()
//                .id(orderTableRequest.getId())
//                .tableGroup(tableGroup)
//                .numberOfGuests(orderTableRequest.getNumberOfGuests())
//                .empty(orderTableRequest.isEmpty())
//                .build();
        return new OrderTable(orderTableRequest.getId(), tableGroup, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
    }
}