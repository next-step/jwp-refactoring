package kitchenpos.application.table;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.exception.InvalidEntityException;
import kitchenpos.exception.order.InvalidOrderStatusException;
import kitchenpos.exception.order.InvalidOrderTableException;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.table.TableGroup.EMPTY;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toEntity();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InvalidEntityException("Not Found OrderTable " + orderTableId));

        if (savedOrderTable.isTableGroupEmpty()) {
            throw new IllegalArgumentException("Already setting another TableGroup");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException("Invalid orderTable Status");
        }

        savedOrderTable.changeEmptyTable();

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final Integer numberOfGuests) {

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InvalidEntityException("Not found OrderTableId " + orderTableId));

        if (savedOrderTable.isEmpty()) {
            throw new InvalidOrderTableException("orderTable id " + savedOrderTable.getId());
        }

        savedOrderTable.changeNumberOfGuest(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }

    public OrderTable getOrderTable(long id) {
        return orderTableRepository.findById(id).orElseThrow(() -> new InvalidEntityException("Not found OrderTable Id" + id));
    }

    public List<OrderTable> getAllOrderTablesByIds(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> getAllOrderTablesByGroupId(long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    public void makeTableGroupEmpty(OrderTable orderTable){
        orderTable.changeTableGroup(EMPTY);
        orderTableRepository.save(orderTable);
    }

    public OrderTable save(OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }
}
