package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ExistsOrderPort existsOrderPort;

    public TableService(final OrderTableRepository orderTableRepository, final ExistsOrderPort existsOrderPort) {
        this.orderTableRepository = orderTableRepository;
        this.existsOrderPort = existsOrderPort;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        boolean completedOrderTable = existsOrderStatusCookingOrMeal(orderTableId);
        orderTable.changeEmpty(orderTableRequest.isEmpty(), completedOrderTable);
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    private boolean existsOrderStatusCookingOrMeal(Long orderTableId) {
        return existsOrderPort.existsOrderStatusCookingOrMeal(orderTableId);
    }
}
