package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;

    public TableService(final TableRepository tableRepository, final OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableResponse create(final TableRequest request) {
        OrderTable orderTable = tableRepository.save(toEntity(request));
        return TableResponse.of(orderTable);
    }

    private OrderTable toEntity(final TableRequest request) {
        return new OrderTable(
                null,
                request.getNumberOfGuests(),
                request.getEmpty());
    }

    public List<TableResponse> list() {
        return tableRepository.findAll()
                .stream()
                .map(orderTable -> TableResponse.of(orderTable))
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeEmpty(final Long orderTableId, final boolean empty) {
        validateOrder(orderTableId);

        OrderTable orderTable = tableRepository.getById(orderTableId);
        orderTable.changeEmpty(empty);
    }

    private void validateOrder(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.NOT_COMPLETED)) {
            throw new IllegalStateException("조리 혹은 식사 상태인 주문이 있어서 빈 테이블로 설정할 수 없습니다. id: " + orderTableId);
        }
    }

    public boolean hasCookingOrMeal(final OrderTable orderTable) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), OrderStatus.NOT_COMPLETED);
    }

    @Transactional
    public void changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable orderTable = tableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);
    }
}
