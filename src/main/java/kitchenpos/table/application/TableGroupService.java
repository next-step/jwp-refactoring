package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = validateCreate(request);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.addOrderTables(orderTables);
        return TableGroupResponse.of(tableGroup);
    }

    private List<OrderTable> validateCreate(TableGroupRequest request) {
        List<Long> orderTableIds = validateNotEmptyIds(request);
        return validateExistsAllOrderTables(orderTableIds);
    }

    private List<OrderTable> validateExistsAllOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블이 있습니다.");
        }
        return orderTables;
    }

    private List<Long> validateNotEmptyIds(TableGroupRequest request) {
        List<Long> orderTableIds = request.toOrderTableId();
        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new IllegalArgumentException("단체 지정할 테이블이 없습니다.");
        }
        return orderTableIds;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
        tableValidator.validateUngroup(tableGroup);
        tableGroup.ungroup();
    }
}
