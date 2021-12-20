package kichenpos.table.table.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableCommandService {

    private final OrderTableRepository orderTableRepository;

    public TableCommandService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable save(OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public OrderTable changeEmpty(long id, boolean empty) {
        OrderTable table = orderTableRepository.table(id);
        table.changeEmpty(empty);
        return table;
    }

    public OrderTable changeNumberOfGuests(long id, Headcount numberOfGuests) {
        OrderTable orderTable = orderTableRepository.table(id);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
