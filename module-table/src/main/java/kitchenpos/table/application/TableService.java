package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.NumberOfGuest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableEmpty;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(OrderTableRepository orderTableRepository,
                        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = getOrderTable(orderTableRequest);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findAllTables() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.ofList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = findOrderTable(orderTableId);
        checkExistTableGroup(orderTable.getId());
        Orders orders = findOrders(orderTable);
        orderTable.changeEmpty(new TableEmpty(orderTableRequest.getEmpty()), orders);
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(new NumberOfGuest(orderTableRequest.getNumberOfGuests()));
        return OrderTableResponse.from(savedOrderTable);
    }

    private Orders findOrders(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        return new Orders(orders);
    }

    private OrderTable getOrderTable(OrderTableRequest orderTableRequest) {
        return new OrderTable(
                new NumberOfGuest(orderTableRequest.getNumberOfGuests()),
                new TableEmpty(orderTableRequest.getEmpty())
        );
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테이블이 등록되어있지 않습니다."));
    }

    private void checkExistTableGroup(Long orderTableId) {
        Long tableGroupId = orderTableRepository.findTableGroupId(orderTableId);
        if (tableGroupId != null){
            throw new IllegalArgumentException("[ERROR] 그룹이 지정 되어있는 경우 빈테이블 여부 업데이트 할수 없습니다.");
        }
    }
}
