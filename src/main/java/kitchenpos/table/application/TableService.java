package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        OrderTable orderTable = findById(orderTableId);
        validateOrderTable(orderTable);
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest request) {
        int numberOfGuests = request.getNumberOfGuests();
        request.validateNumberOfGuests();
        OrderTable savedOrderTable = findById(orderTableId);
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블은 손님 수를 변경할 수 없습니다.");
        }
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTableResponse findResponseById(Long id) {
        return OrderTableResponse.from(findById(id));
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("OrderTable id:" + id + "가 존재하지 않습니다."));
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("테이블이 속한 테이블 그룹이 있으면 테이블을 비울 수 없습니다.");
        }
        if (orderTable.isNotPaymentFinished()) {
            throw new IllegalArgumentException("아직 계산을 마치지 않은 테이블은 비울 수 없습니다.");
        }
    }
}
