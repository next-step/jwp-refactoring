package kitchenpos.table.service;

import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableGroupService orderTableGroupService;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableGroupService orderTableGroupService) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableGroupService = orderTableGroupService;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        checkGroupValidation(request);
        final TableGroup save = tableGroupRepository.save(new TableGroup());
        final List<OrderTableResponse> orderTableResponses = orderTableGroupService.saveAll(save, request.getOrderTables());
        return TableGroupResponse.of(save, orderTableResponses);
    }

    private void checkGroupValidation(final TableGroupRequest request) {
        if (request.getOrderTables().isEmpty() || request.getOrderTables().size() < 2) {
            throw new IllegalArgumentException("단체 테이블을 지정할 수 없습니다.");
        }
    }

    public void ungroup(long id) {
        final TableGroup groupById = findGroupById(id);
        orderTableGroupService.ungroup(groupById);
        tableGroupRepository.delete(groupById);
    }

    @Transactional(readOnly = true)
    public TableGroup findGroupById(long id) {
        return tableGroupRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹을 찾을수 없습니다."));
    }
}
