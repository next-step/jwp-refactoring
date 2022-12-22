package kitchenpos.table.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderTableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = OrderTable.create(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        OrderTable createdOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(createdOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> menuGroups = orderTableRepository.findAllJoinFetch();
        return menuGroups.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = getOrderTable(orderTableId);

        validateExistsOrderTable(orderTableId);

        savedOrderTable.updateEmpty(orderTableRequest.isEmpty());
        OrderTable updatedOrderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.from(updatedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTableValidator.validate(orderTableRequest.getNumberOfGuests());

        OrderTable savedOrderTable = getOrderTable(orderTableId);

        savedOrderTable.updateNumberOfGuests(orderTableRequest.getNumberOfGuests());
        OrderTable updatedOrderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.from(updatedOrderTable);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException("OrderTable", orderTableId));
    }

    private void validateExistsOrderTable(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문의 상태가 요리중이거나 식사중이어선 안됩니다.");
        }
    }

}
