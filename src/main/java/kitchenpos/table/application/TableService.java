package kitchenpos.table.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeTableEmptyRequest;
import kitchenpos.table.dto.ChangeTableGuestRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    public TableResponse create(final TableRequest tableRequest) {
        return TableResponse.from(orderTableRepository.save(tableRequest.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    public TableResponse changeEmpty(final Long orderTableId, final ChangeTableEmptyRequest changeTableEmptyRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateEmpty(changeTableEmptyRequest.isEmpty());

        return TableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId, final ChangeTableGuestRequest changeTableGuestRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.updateNumberOfGuest(changeTableGuestRequest.getNumberOfGuests());

        return TableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
