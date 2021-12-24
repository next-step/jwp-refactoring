package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableChangeEmptyEvent;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.repository.table.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableService(OrderTableRepository orderTableRepository, ApplicationEventPublisher eventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {

        return orderTableRepository.save(new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()));
    }
    @Transactional(readOnly=true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        eventPublisher.publishEvent(new OrderTableChangeEmptyEvent(savedOrderTable.getId()));
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {

        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        savedOrderTable.checkIsEmpty();
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    public OrderTable getOrderTable(Long id) {
        return orderTableRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));
    }

    public List<OrderTable> getOrderTablesByTableGroup(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
