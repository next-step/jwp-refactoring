package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.event.TableChangeEmptyEvent;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private static final String ORDER_TABLE_IS_NOT_EXIST = "주문테이블이 존재하지 않습니다";
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableService(OrderTableRepository orderTableRepository, ApplicationEventPublisher eventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST));
        eventPublisher.publishEvent(new TableChangeEmptyEvent(orderTableId));
        savedOrderTable.changeEmpty(empty);
        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST));
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }
}
