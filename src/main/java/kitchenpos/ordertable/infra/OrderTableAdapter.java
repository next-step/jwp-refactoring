package kitchenpos.ordertable.infra;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableDomainService;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTableAdapter implements OrderTableDomainService {
    private final OrderTableRepository orderTableRepository;

    public OrderTableAdapter(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public OrderTable findAvailableTableForOrder(Long id) {
        return orderTableRepository.findById(id)
                .filter(OrderTable::isNotEmpty)
                .orElseThrow(() -> new IllegalArgumentException("주문을 생성할 수 있는 테이블이 존재하지 않습니다."));
    }
}
