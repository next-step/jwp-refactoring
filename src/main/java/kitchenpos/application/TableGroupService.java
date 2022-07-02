package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        TableGroup.validateInputOrderTable(request.getOrderTables());

        final List<Long> orderTableIds = request.getOrderTables()
                                                .stream()
                                                .map(OrderTableRequest::getId)
                                                .collect(Collectors.toList());

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validatePresentOrderTable(request.getOrderTableSize(), orderTables.size());

        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(IllegalArgumentException::new);

        tableGroup.getOrderTables()
                  .forEach(orderTable -> {
                      List<Order> orders = orderRepository.findByOrderTable(orderTable);
                      if (orders.stream().anyMatch(Order::hasOrderStatusInCookingOrMeal)) {
                          throw new IllegalArgumentException("주문 상태가 조리 또는 식사인 테이블은 주문 등록 가능 상태를 변경할 수 없습니다.");
                      }});
        tableGroup.ungroup();
    }

    private void validatePresentOrderTable(int inputSize, int presentSize) {
        if (inputSize != presentSize) {
            throw new IllegalArgumentException("요청 테이블과 실제 테이블 개수가 일치하지 않습니다.");
        }
    }
}
