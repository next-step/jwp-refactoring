package kitchenpos.table;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dto.table.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponse::from)
            .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.validateForChangeEmpty();
        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTable orderTable) {
        final NumberOfGuests numberOfGuests = orderTable.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
