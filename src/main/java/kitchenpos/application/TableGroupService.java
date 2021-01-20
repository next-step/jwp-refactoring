package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableResponse;
import kitchenpos.exception.NotFoundEntityException;
import kitchenpos.exception.TableInUseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, TableRepository tableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        List<Long> tableIds = request.getTableIds();
        List<OrderTable> orderTables = tableRepository.findAllById(tableIds);

        if (orderTables.size() != tableIds.size()) {
            throw new NotFoundEntityException("등록되지 않은 테이블은 그룹에 포함시킬 수 없습니다");
        }

        OrderTables tables = new OrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(tables));
        return fromEntity(savedTableGroup);
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(EntityNotFoundException::new);

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                tableGroup.getOrderTables(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
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
