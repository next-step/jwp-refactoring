package kitchenpos.order.application;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderService orderService,
                        final OrderTableRepository orderTableRepository
    ) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        return orderTableRepository.save(orderTableRequest.toOrderTable());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {

        final OrderTable orderTable = orderService.findOrderTableById(orderTableId);
        orderTable.validateTableGroupIsNull();
        orderTable.validateOrderStatusNotInCookingAndMeal();
        orderTable.updateEmpty(orderTableRequest.getEmpty());

        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        orderTableRequest.validateNumberOfGuests();
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderService.findOrderTableById(orderTableId);
        validateOrderTableIsEmpty(savedOrderTable);

        savedOrderTable.updateNumberOfGuests(numberOfGuests);
        return orderTableRepository.save(savedOrderTable);
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    private void validateOrderTableIsEmpty(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new OrderTableException("주문 테이블이 비어있습니다");
        }
    }
}
