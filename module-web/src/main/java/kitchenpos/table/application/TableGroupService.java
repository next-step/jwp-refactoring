package kitchenpos.table.application;


import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;


@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = new TableGroup(
                new OrderTables(
                        orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds())
                )
        );
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        tableGroup.unGroupingTable();
    }
}
