package kitchenpos.table.application;

import kitchenpos.order.validator.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderValidator orderValidator
    ) {
        this.orderValidator = orderValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        orderValidator.validateOrderComplete(orderTableId);
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTable) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
