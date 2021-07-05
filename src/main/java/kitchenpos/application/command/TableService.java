package kitchenpos.application.command;

import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableCreate;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }


    public Long create(final OrderTableCreate create) {
        return orderTableRepository.save(OrderTable.from(create))
                .getId();
    }


    public void changeEmpty(final Long orderTableId, boolean empty) {
        final OrderTable orderTable = findById(orderTableId);

        orderTable.changeEmpty(empty);
    }

    public void changeNumberOfGuests(final Long orderTableId, final NumberOfGuest numberOfGuest) {
        OrderTable orderTable = findById(orderTableId);

        orderTable.changeNumberOfGuest(numberOfGuest);
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
