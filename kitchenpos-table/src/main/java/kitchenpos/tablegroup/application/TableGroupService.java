package kitchenpos.tablegroup.application;

import kitchenpos.exception.TableInUseException;
import kitchenpos.support.OrderSupport;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.TableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.tablegroup.dao.TableGroupRepository;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final OrderSupport orderSupport;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderSupport orderSupport, TableRepository tableRepository, TableGroupRepository tableGroupRepository) {
        this.orderSupport = orderSupport;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        List<Long> tableIds = request.getTableIds();
        List<OrderTable> orderTables = tableRepository.findAllById(tableIds);

        if (orderTables.size() != tableIds.size()) {
            throw new EntityNotFoundException("등록되지 않은 테이블은 그룹에 포함시킬 수 없습니다");
        }

        OrderTables tables = new OrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(tables));
        savedTableGroup.group();
        return fromEntity(savedTableGroup);
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(EntityNotFoundException::new);

        if (orderSupport.isUsingTables(tableGroup.getOrderTables())) {
            throw new TableInUseException("사용중인 테이블의 그룹은 해제할 수 없습니다");
        }

        tableGroup.unGroup();
        tableGroupRepository.delete(tableGroup);
    }

    private TableGroupResponse fromEntity(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                fromTableEntities(tableGroup.getOrderTables()));
    }

    private List<TableResponse> fromTableEntities(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableService::fromEntity)
                .collect(Collectors.toList());
    }
}
