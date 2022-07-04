package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable saveTable = orderTableRepository.save(
                OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()));

        return OrderTableResponse.formOrderTable(saveTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("변경할 테이블이 존재하지 않습니다."));

        validateChangeEmptyTable(orderTableId);

        savedOrderTable.changeEmptyTable();

        return OrderTableResponse.formOrderTable(savedOrderTable);
    }

    private void validateChangeEmptyTable(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료된 상태여만 빈테이블로 변경이 가능합니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("오더 테이블이 존재하지 않습니다."));

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.formOrderTable(savedOrderTable);
    }
}
