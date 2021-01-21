package kitchenpos.table;

import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private static final String ERR_TEXT_STATUS_IS_COOKING_OR_MEAL = "조리 또는 식사중인 경우 빈 테이블로 설정할 수 없습니다.";
    private static final String ERR_TEXT_CHANGE_GUEST_MUST_BE_GREATER_THAN_ZERO = "변경할 게스트의 수는 1명 이상이어야 합니다.";
    private static final int IS_LESS_THAN_ZERO = 0;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable newOrderTable =
            OrderTable.of(orderTableRequest.getTableGroupId(), orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());

        return new OrderTableResponse(orderTableRepository.save(newOrderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        foundOrderTable.throwIllegalExceptionWhenTableGroupIsNull();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ERR_TEXT_STATUS_IS_COOKING_OR_MEAL);
        }

        foundOrderTable.setEmpty(orderTableRequest.isEmpty());

        return new OrderTableResponse(orderTableRepository.save(foundOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = validateNumberOfGuestsAreGreaterThanZero(orderTableRequest);

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.throwIllegalExceptionWhenOrderTableIsEmpty();

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return new OrderTableResponse(orderTableRepository.save(savedOrderTable));
    }

    private int validateNumberOfGuestsAreGreaterThanZero(final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        if (numberOfGuests < IS_LESS_THAN_ZERO) {
            throw new IllegalArgumentException(ERR_TEXT_CHANGE_GUEST_MUST_BE_GREATER_THAN_ZERO);
        }

        return numberOfGuests;
    }
}
