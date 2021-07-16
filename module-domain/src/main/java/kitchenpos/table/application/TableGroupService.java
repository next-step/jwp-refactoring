package kitchenpos.table.application;

import kitchenpos.exception.DuplicateOrderTableException;
import kitchenpos.exception.FailedUngroupException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableDependencyHelper;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TableGroupService {
    private final TableDependencyHelper tableDependencyHelper;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableDependencyHelper tableDependencyHelper,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.tableDependencyHelper = tableDependencyHelper;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new DuplicateOrderTableException("단체 지정시 주문 테이블은 중복될 수 없습니다.");
        }

        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));
        return TableGroupResponse.from(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(EntityNotFoundException::new);
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();
        if (tableDependencyHelper.existsByOrderTableIdInAndOrderStatusNotCompletion(orderTableIds)) {
            throw new FailedUngroupException("주문 상태가 모두 완료일때만 단체 지정해제가 가능합니다.");
        }
        tableGroup.ungroup();
    }
}
