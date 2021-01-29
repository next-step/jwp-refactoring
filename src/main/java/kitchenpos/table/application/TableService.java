package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();

        return OrderTableResponse.of(this.orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return this.orderTableRepository.findAll().stream().map(OrderTableResponse::of).collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = this.orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        this.canEmptySavedOrderTable(orderTableId, savedOrderTable);

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return OrderTableResponse.of(this.orderTableRepository.save(savedOrderTable));
    }

    /**
     * 비울 수 있는 테이블인지 확인합니다.
     * @param orderTableId
     * @param savedOrderTable
     */
    private void canEmptySavedOrderTable(Long orderTableId, OrderTable savedOrderTable) {
        savedOrderTable.notExistTabeGroup();
        this.validateOrderTablesByIdsAndStatus(orderTableId);
    }


    /**
     * 주문 테이블들의 ID들과 상태가 적합한지 검사합니다.
     * @param orderTableId
     */
    private void validateOrderTablesByIdsAndStatus(Long orderTableId) {
        if (this.orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId
                                                , final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.toOrderTable().getNumberOfGuests();

        final OrderTable savedOrderTable = this.orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        this.checkEmptySavedOrderTable(savedOrderTable);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(this.orderTableRepository.save(savedOrderTable));
    }

    /**
     * 주문 테이블이 비었는지 확인합니다.
     * @param savedOrderTable
     * @throws IllegalArgumentException
     */
    private void checkEmptySavedOrderTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

}
