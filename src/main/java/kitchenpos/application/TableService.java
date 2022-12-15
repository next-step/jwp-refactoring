package kitchenpos.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.NotExistIdException;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(request));
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::of)
            .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NotExistIdException::new);
        orderTable.changeEmpty(empty);
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final Integer numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NotExistIdException::new);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(orderTable);
    }

}
