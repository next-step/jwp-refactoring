package kitchenpos.ui.creator;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableCreator {

    private final TableGroupRepository tableGroupRepository;

    public OrderTableCreator(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    public OrderTable toOrderTable(OrderTableRequest orderTableRequest) {
        TableGroup tableGroup = getTableGroupById(orderTableRequest);

        return OrderTable.builder()
                .id(orderTableRequest.getId())
                .tableGroup(tableGroup)
                .numberOfGuests(orderTableRequest.getNumberOfGuests())
                .empty(orderTableRequest.isEmpty())
                .build();
    }

    private TableGroup getTableGroupById(OrderTableRequest orderTableRequest) {
        TableGroup tableGroup = null;
        Long tableGroupId = orderTableRequest.getTableGroupId();

        if (tableGroupId != null) {
            tableGroup = tableGroupRepository.findById(tableGroupId).orElse(null);
        }
        return tableGroup;
    }
}