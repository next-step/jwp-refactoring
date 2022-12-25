package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.validator.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator validator;

    public TableService(OrderTableRepository orderTableRepository,
        OrderTableValidator validator) {
        this.orderTableRepository = orderTableRepository;
        this.validator = validator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        validator.validateChangeEmpty(orderTable);
        orderTable.changeEmpty(isEmpty);
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final int numberOfGuests) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuest(numberOfGuests);
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("변경하고자 하는 테이블 정보가 없습니다."));
    }
}
