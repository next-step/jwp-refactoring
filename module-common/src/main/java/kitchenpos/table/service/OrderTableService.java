package kitchenpos.table.service;

import kitchenpos.order.service.OrderStatusService;
import kitchenpos.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderStatusService orderstatusService;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderStatusService orderstatusService) {
        this.orderTableRepository = orderTableRepository;
        this.orderstatusService = orderstatusService;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        checkNumberOfGuestsLessThanZero(request.getNumberOfGuests());
        final OrderTable savedTable = orderTableRepository.save(request.toTable());
        return OrderTableResponse.of(savedTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long id, boolean empty) {
        final OrderTable tableById = findById(id);
        if (orderstatusService.isNotCompleteOrder(tableById.getId())) {
            throw new IllegalArgumentException("주문이 완료되지 않아 빈 테이블로 바꿀수 없습니다.");
        }
        tableById.changeEmpty(empty);

        return OrderTableResponse.of(tableById);
    }

    public OrderTableResponse changeNumberOfGuests(Long id, final OrderTableRequest orderTableRequest) {
        int numberOfGuests = orderTableRequest.getNumberOfGuests();
        checkNumberOfGuestsLessThanZero(numberOfGuests);
        final OrderTable tableById = findById(id);
        tableById.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(tableById);
    }

    private void checkNumberOfGuestsLessThanZero(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 인원은 0보다 작을수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("테이블을 찾을수 없습니다."));
    }
}
