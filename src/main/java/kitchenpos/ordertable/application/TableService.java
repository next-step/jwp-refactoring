package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private static final String ERROR_MESSAGE_TABLE_NOT_EXIST = "존재하지 않는 주문 테이블입니다.";
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest requestOrderTable) {
        OrderTable orderTable = new OrderTable(
            new NumberOfGuests(requestOrderTable.getNumberOfGuests()),
            requestOrderTable.isOrderClose());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.fromList(orderTables);
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_TABLE_NOT_EXIST));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
        final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.updateTableStatus(orderTableRequest.isOrderClose());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableRequest orderTableRequest) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(
            orderTableRequest.getNumberOfGuests());
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTable> findOrderTables(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
            .map(orderTableRequest -> findOrderTable(orderTableRequest.getId()))
            .collect(Collectors.toList());
    }
}
