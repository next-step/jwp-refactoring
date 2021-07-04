package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.CreateOrderTableDto;
import kitchenpos.table.exception.NotChangeNumberOfGuestsException;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableDto createOrderTableDto) {
        return orderTableRepository.save(new OrderTable(createOrderTableDto.getNumberOfGuests(),
                                                        createOrderTableDto.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable savedOrderTable =
            orderTableRepository.findById(orderTableId)
                                .orElseThrow(() -> new NotFoundOrderTableException(orderTableId));

        savedOrderTable.changeEmpty(empty);
        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {

        if (numberOfGuests < 0) {
            throw new NotChangeNumberOfGuestsException("변경할 손님 수는 0 이상의 수만 입력할 수 있습니다.");
        }

        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                         .orElseThrow(() -> new NotFoundOrderTableException(orderTableId));

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }
}
