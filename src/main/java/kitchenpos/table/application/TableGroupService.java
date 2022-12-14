package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.dao.OrderDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private static final String ERROR_MESSAGE_NOT_FOUND_GROUP_TABLE = "존재하지 않는 단체 테이블입니다. ID : %d";

    private final OrderDao orderDao;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao, TableService tableService, TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> requestTables = findOrderTable(request.getOrderTables());
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.from(requestTables));
        tableGroup.orderTables().updateTableGroup(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        OrderTables orderTables = tableGroup.orderTables();
//        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }
        orderTables.ungroup();
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_GROUP_TABLE, tableGroupId)));
    }

    private List<OrderTable> findOrderTable(List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(tableService::findById)
                .collect(Collectors.toList());
    }
}
