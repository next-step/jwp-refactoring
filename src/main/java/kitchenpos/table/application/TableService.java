package kitchenpos.table.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableGuestRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderStatusManagementService orderStatusManagementService;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderStatusManagementService orderStatusManagementService,
        OrderTableRepository orderTableRepository) {
        this.orderStatusManagementService = orderStatusManagementService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(),
            orderTableRequest.isEmpty());

        OrderTable saveOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(saveOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableStatusRequest orderTableStatusRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if(Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        orderStatusManagementService.validateOrderStatusBeChanged(orderTableId);
        savedOrderTable.reserve(orderTableStatusRequest.isEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableGuestRequest orderTableGuestRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeGuests(orderTableGuestRequest.getNumberOfGuests());

        return OrderTableResponse.from(savedOrderTable);
    }
}
