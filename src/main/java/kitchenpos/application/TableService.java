package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;


    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
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
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));

        savedOrderTable.checkIsEmpty();
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    public OrderTable getOrderTable(Long id) {
        return orderTableRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));
    }

    public List<OrderTable> getOrderTablesByTableGroup(TableGroup tableGroup) {
        return orderTableRepository.findAllByTableGroup(tableGroup);
    }
}
