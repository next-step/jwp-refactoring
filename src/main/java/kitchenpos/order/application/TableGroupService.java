package kitchenpos.order.application;

import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.dao.TableGroupRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = findOrderTables(tableGroupRequest.getOrderTableIds());
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup(orderTables)));
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        if (!savedOrderTables.isSameSize(orderTableIds.size())) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 포함되어 있습니다.");
        }
        return savedOrderTables;
    }

    @Transactional
    public void unGroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 그룹 입니다."));
        tableGroup.unGroup();
        tableGroupRepository.save(tableGroup);
    }
}
