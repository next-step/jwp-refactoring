package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequestDto;
import kitchenpos.table.dto.OrderTableResponseDto;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponseDto create(final OrderTableRequestDto request) {
        OrderTable orderTable = orderTableRepository.save(request.toEntity());
        return new OrderTableResponseDto(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponseDto> list() {
        List<OrderTable> list = orderTableRepository.findAll();
        return list.stream()
            .map(OrderTableResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponseDto changeEmpty(final Long orderTableId) {
        OrderTable orderTable = getOrderTable(orderTableId);
        tableValidator.checkValidChangeEmpty(orderTable);
        orderTable.changeEmpty();
        return new OrderTableResponseDto(orderTable);
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuest) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuest(numberOfGuest);
        return new OrderTableResponseDto(orderTable);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);
    }
}
