package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequestDto;
import kitchenpos.dto.OrderTableResponseDto;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponseDto create(final OrderTableRequestDto request) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(request));
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
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);

        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        orderTable.changeEmpty();
        return new OrderTableResponseDto(orderTable);
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuest) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);
        orderTable.changeNumberOfGuest(numberOfGuest);
        return new OrderTableResponseDto(orderTable);
    }
}
