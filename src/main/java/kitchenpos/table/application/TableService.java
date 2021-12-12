package kitchenpos.table.application;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.request.TableGuestsCountRequest;
import kitchenpos.table.ui.request.TableStatusRequest;
import kitchenpos.table.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listFrom(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final long orderTableId,
        final TableStatusRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.hasTableGroup()) {
            throw new IllegalArgumentException();
        }

        if (savedOrderTable.isCookingOrMeal()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setStatus(request.status());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final long orderTableId,
        final TableGuestsCountRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(Headcount.from(numberOfGuests));
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }

    public Optional<OrderTable> findById(long orderTableId) {
        return orderTableRepository.findById(orderTableId);
    }
}
