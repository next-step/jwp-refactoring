package kitchenpos.table.application;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupMapper {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupMapper(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroup mapFrom(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = new TableGroup(orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds()));
        return tableGroupRepository.save(tableGroup);
    }
}
