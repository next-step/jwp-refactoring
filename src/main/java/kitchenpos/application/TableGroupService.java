package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableEntity;
import kitchenpos.domain.TableGroupEntity;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao, TableService tableService, TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTableEntity> orderTables = request.getOrderTableIds()
                .stream()
                .map(id -> tableService.findOrderTableById(id))
                .collect(Collectors.toList());

        TableGroupEntity tableGroup = tableGroupRepository.save(new TableGroupEntity(orderTables));

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroupEntity tableGroup = findTableGroupById(tableGroupId);

        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableEntity::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 테이블들의 상태가 조리 혹은 식사이기 때문에 단체 지정 해제할 수 없습니다.");
        }

        tableGroup.ungroup();
    }

    public TableGroupEntity findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("테이블 그룹을 찾을 수 없습니다: " + id));
    }
}
