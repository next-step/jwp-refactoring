package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableEntity;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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
        OrderTableEntity orderTable = tableRepository.save(
                new OrderTableEntity(
                        null,
                        request.getNumberOfGuests(),
                        request.getEmpty()
                )
        );

        return TableResponse.of(orderTable);
    }

    public List<TableResponse> list() {
        return tableRepository.findAll()
                .stream()
                .map(orderTable -> TableResponse.of(orderTable))
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeEmpty(final Long orderTableId, final boolean empty) {
        OrderTableEntity orderTable = findOrderTableById(orderTableId);

        if (hasCookingOrMeal(orderTable)) {
            throw new IllegalStateException("조리 혹은 식사 상태인 테이블이 있어서 빈 테이블로 설정할 수 없습니다: " + orderTableId);
        }

        orderTable.changeEmpty(empty);
    }

    public boolean hasCookingOrMeal(OrderTableEntity orderTable) {
        return orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    @Transactional
    public void changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTableEntity orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);
    }

    public OrderTableEntity findOrderTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("주문 테이블을 찾을 수 없습니다: " + id));
    }
}
