package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableChangeableCheckRequestEvent;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableService(OrderTableRepository orderTableRepository, ApplicationEventPublisher eventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {

        return OrderTableResponse.from(orderTableRepository.save(new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty())));
    }
    @Transactional(readOnly=true)
    public List<OrderTableResponse> list() {

        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        eventPublisher.publishEvent(new OrderTableChangeableCheckRequestEvent(savedOrderTable.getId()));
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {

        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        savedOrderTable.checkIsEmpty();
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    public OrderTable getOrderTable(Long id) {
        return orderTableRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));
    }

    public List<OrderTable> getOrderTablesByTableGroup(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
