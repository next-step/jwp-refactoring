package kitchenpos.table.application;

import java.util.List;
import kitchenpos.exception.NotChangeNumberOfGuestsException;
import kitchenpos.exception.NotFoundEntityException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.CreateOrderTableDto;
import kitchenpos.table.dto.OrderTableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto create(final CreateOrderTableDto createOrderTableDto) {
        return OrderTableDto.of(
            orderTableRepository.save(new OrderTable(createOrderTableDto.getNumberOfGuests(),
                                                     createOrderTableDto.isEmpty())));
    }

    public List<OrderTableDto> list() {
        return orderTableRepository.findAll()
                                   .stream()
                                   .map(OrderTableDto::of)
                                   .collect(toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable savedOrderTable =
            orderTableRepository.findById(orderTableId)
                                .orElseThrow(NotFoundEntityException::new);

        savedOrderTable.changeEmpty(empty);
        return OrderTableDto.of(savedOrderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {

        if (numberOfGuests < 0) {
            throw new NotChangeNumberOfGuestsException("변경할 손님 수는 0 이상의 수만 입력할 수 있습니다.");
        }

        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                         .orElseThrow(NotFoundEntityException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableDto.of(savedOrderTable);
    }
}
