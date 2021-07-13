package kitchenpos.application;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.repository.OrderTableRepository;
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

        final OrderTable savedOrderTable = orderService.findOrderTableById(orderTableId);
        savedOrderTable.validateTableGroupIsNull();

        orderService.validateOrderStatusNotIn(savedOrderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
        savedOrderTable.updateEmpty(orderTableRequest.getEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        orderTableRequest.validateNumberOfGuests();
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderService.findOrderTableById(orderTableId);
        validateOrderTableIsEmpty(savedOrderTable);

        savedOrderTable.updateNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findAllByTableGroup(TableGroup tableGroup) {
        return orderTableRepository.findAllByTableGroup(tableGroup);
    }

    private void validateOrderTableIsEmpty(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new OrderTableException("주문 테이블이 비어있습니다");
        }
    }
}
