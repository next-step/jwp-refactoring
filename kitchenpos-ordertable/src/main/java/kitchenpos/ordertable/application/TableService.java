package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.common.event.OrderTableChangeOrderCloseEvent;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.TableNotFoundException;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
//    private final OrderValidator orderTableValidator;


    public TableService(OrderTableRepository orderTableRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest requestOrderTable) {
        OrderTable orderTable = new OrderTable(
            new NumberOfGuests(requestOrderTable.getNumberOfGuests()),
            requestOrderTable.isOrderClose());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.fromList(orderTables);
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(TableNotFoundException::new);
    }

    @Transactional
    public OrderTableResponse changeOrderClose(final Long orderTableId,
        final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        applicationEventPublisher.publishEvent(
            new OrderTableChangeOrderCloseEvent(this, savedOrderTable.getId()));
        savedOrderTable.updateTableStatus(orderTableRequest.isOrderClose());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableRequest orderTableRequest) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(
            orderTableRequest.getNumberOfGuests());
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }
}
