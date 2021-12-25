package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableGroupValidator;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@Service
@Transactional
public class TableGroupService {
    private static final int MIN_TABLE_SIZE = 2;

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
        final TableGroupRepository tableGroupRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = getOrderTablesToGroup(orderTableIds);
        tableGroupValidator.validateToGroup(orderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        savedTableGroup.changeOrderTablesToNotEmpty();

        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("단체 지정을 찾을 수 없습니다"));
        tableGroupValidator.validateToUngroup(tableGroup);

        tableGroup.ungroup();
    }

    private List<OrderTable> getOrderTablesToGroup(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException("단체 지정할 주문 테이블은 2개 이상이어야 합니다");
        }

        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블의 아이디가 부정확합니다");
        }
        return orderTables;
    }
}
