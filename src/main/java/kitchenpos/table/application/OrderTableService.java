package kitchenpos.table.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }
}
