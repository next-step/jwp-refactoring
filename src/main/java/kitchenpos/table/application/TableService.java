package kitchenpos.table.application;

import java.util.stream.Collectors;
import kitchenpos.common.Empty;
import kitchenpos.common.GuestCount;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderService orderService, OrderTableRepository orderTableRepository) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());

    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        OrderTable saved = findOrderTableById(orderTableId);
        boolean isAllFinished = orderService.isAllOrderFinished(orderTableId);
        if(!isAllFinished){
            throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_EMPTINESS_WHEN_ORDER_NOT_COMPLETED);
        }
        saved.updateEmptyStatus(Empty.of(empty));
        return OrderTableResponse.of(saved);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int guestCounts) {
        OrderTable saved = findOrderTableById(orderTableId);
        saved.updateNumberOfGuest(GuestCount.of(guestCounts));
        return OrderTableResponse.of(saved);
    }

    public OrderTable findOrderTableById(Long orderTableId){
        return orderTableRepository.findById(orderTableId).orElseThrow(
                ()->new EntityNotFoundException("주문테이블", orderTableId)
        );
    }
}
