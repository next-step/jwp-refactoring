package kichenpos.table.application;

import kichenpos.table.domain.OrderTable;
import kichenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableValidator orderTableValidator, OrderTableRepository orderTableRepository) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        orderTableValidator.validateChangeEmpty(orderTableId);

        savedOrderTable.changeEmpty(orderTable.isEmpty());
        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return savedOrderTable;
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }
}
