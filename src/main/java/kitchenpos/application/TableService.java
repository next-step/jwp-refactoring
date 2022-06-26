package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableEntity;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
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
    private final OrderDao orderDao;
    private final TableRepository tableRepository;

    public TableService(final OrderDao orderDao, TableRepository tableRepository) {
        this.orderDao = orderDao;
        this.tableRepository = tableRepository;
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
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        OrderTableEntity orderTable = findOrderTableById(orderTableId);
        orderTable.changeEmpty(empty);
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
