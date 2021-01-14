package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao,
          OrderTableService orderTableService,
          TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> savedOrderTables = orderTableService
              .findAllByOrderTableIds(request.getOrderTableIds());

        OrderTables orderTables = new OrderTables(savedOrderTables, request.getRequestSize());

        TableGroup savedTableGroup = tableGroupRepository.save(request.toEntity(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);

        List<Long> orderTableIdList = tableGroup.getOrderTables().getOrderTables().stream()
              .map(OrderTable::getId)
              .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
              orderTableIdList, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("단체지정 해제가 불가능한 테이블입니다.");
        }

        tableGroup.unTableGroup();
        tableGroupRepository.deleteById(tableGroup.getId());
    }

    private TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
              .orElseThrow(IllegalArgumentException::new);
    }
}
