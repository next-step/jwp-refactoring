package kitchenpos.order.application.ordertable;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.ordertable.OrderTable;
import kitchenpos.order.domain.ordertable.OrderTableRepository;
import kitchenpos.order.domain.ordertable.TableGroup;
import kitchenpos.order.domain.ordertable.TableGroupRepository;
import kitchenpos.order.dto.ordertable.TableGroupRequest;
import kitchenpos.order.dto.ordertable.TableGroupResponse;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        TableGroup tableGroup = tableGroupRequest.toTableGroup(orderTables);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup();
    }
}
