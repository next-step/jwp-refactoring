package kitchenpos.table;

import kitchenpos.guestordertable.GuestOrderTable;
import kitchenpos.guestordertable.GuestOrderTableService;
import org.springframework.stereotype.Service;

@Service
public class GuestOrderTableServiceImpl implements GuestOrderTableService {

    private final OrderTableRepository orderTableRepository;

    public GuestOrderTableServiceImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public GuestOrderTable getGuestOrderTable(long orderTableId) {
        return this.orderTableRepository.findById(orderTableId)
                .map(orderTable -> new GuestOrderTable(orderTable.getId(), orderTable.isEmpty()))
                .orElseThrow(() -> new IllegalArgumentException("not found empty order table"));
    }

}
