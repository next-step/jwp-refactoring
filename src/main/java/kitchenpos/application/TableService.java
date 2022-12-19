package kitchenpos.application;

import static kitchenpos.exception.ErrorCode.EXISTS_NOT_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.NOT_EXISTS_TABLE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.validator.TableValidator;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTableResponse create(final OrderTable orderTable) {
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderTable findById(Long orderTableId){
        return orderTableRepository.findById(orderTableId).orElseThrow(() -> new KitchenposException(NOT_EXISTS_TABLE));
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = findById(orderTableId);
        TableValidator.validateChangeEmpty(savedOrderTable);
        existsCookingOrMeal(orderTableId);

        savedOrderTable.changeEmpty(empty);

        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        TableValidator.validateNumberOfGuests(orderTable.getNumberOfGuests());

        final int numberOfGuests = orderTable.getNumberOfGuests();

        final OrderTable savedOrderTable = findById(orderTableId);
        TableValidator.validateEmptyTrue(savedOrderTable);

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private void existsCookingOrMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_STATUS);
        }
    }
}
