package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.EntityNotFoundException;
import kitchenpos.common.vo.Empty;
import kitchenpos.common.vo.GuestCount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {

        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
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
        orderTableValidator.changeEmptyOrderStatusValidate(orderTableId);
        saved.updateEmptyStatus(Empty.EMPTY);
        return OrderTableResponse.of(saved);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int guestCounts) {
        OrderTable saved = findOrderTableById(orderTableId);
        saved.updateNumberOfGuest(GuestCount.of(guestCounts));
        return OrderTableResponse.of(saved);
    }

    public OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(
            () -> new EntityNotFoundException(OrderTable.ENTITY_NAME, orderTableId)
        );
    }
}
