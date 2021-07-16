package kitchenpos.orderTable.application;

import kitchenpos.orderTable.event.OrderTableChangedEvent;
import kitchenpos.orderTable.event.OrderTableEmptiedEvent;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableService(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher eventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public OrderTable getOrderTableByOrderTableId(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("등록된 주문 테이블만 상태값을 변경할수 있습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable create(final OrderTable orderTable) {

        eventPublisher.publishEvent(new OrderTableEmptiedEvent(orderTable));

        return orderTableRepository.save(orderTable);
    }

    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = getOrderTableByOrderTableId(orderTableId);

        eventPublisher.publishEvent(new OrderTableChangedEvent(savedOrderTable, orderTable.isEmpty()));

        return savedOrderTable;
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = getOrderTableByOrderTableId(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return savedOrderTable;
    }
}
