package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        request.validateRequestedSizeOfOrderTables();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(request.getOrderTableIds());
        request.validateSavedSizeOfOrderTables(savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.createTableGroup(LocalDateTime.now(), savedOrderTables));
        savedOrderTables.forEach(ot -> ot.occupy(savedTableGroup));
        return TableGroupResponse.from(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        if (tableGroup.isNotPaymentFinished()) {
            throw new IllegalArgumentException("테이블 그룹 중에 아직 결제가 끝나지 않은 주문이 있습니다.");
        }
        tableGroup.clearTables();
    }

    public TableGroup findById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TableGroup id:" + id + "가 존재하지 않습니다."));
    }
}
