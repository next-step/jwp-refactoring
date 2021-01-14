package kitchenpos.table.service;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderTableServiceJpa {
    private final OrderTableRepository orderTableRepository;

    public OrderTableServiceJpa(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable savedTable = orderTableRepository.save(request.toTable());
        return OrderTableResponse.of(savedTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long id, OrderTableRequest orderTableRequest) {
        OrderTable tableById = findById(id);
        tableById.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(tableById);
    }

    public OrderTableResponse changeNumberOfGuests(Long id, OrderTableRequest orderTableRequest) {
        int numberOfGuests = orderTableRequest.getNumberOfGuests();
        checkNumberOfGuestsLessThanZero(numberOfGuests);
        OrderTable tableById = findById(id);
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
