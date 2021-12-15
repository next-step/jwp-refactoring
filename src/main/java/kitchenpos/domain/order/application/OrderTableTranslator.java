package kitchenpos.domain.order.application;

import kitchenpos.domain.order.domain.OrderTableVO;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableTranslator implements TableTranslator {

    private final OrderTableRepository orderTableRepository;

    public OrderTableTranslator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public OrderTableVO getOrderTable(Long orderTableId) {
        final OrderTable table = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        return new OrderTableVO(table.isEmpty());
    }
}
