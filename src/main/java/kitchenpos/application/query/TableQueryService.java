package kitchenpos.application.query;

import kitchenpos.dto.response.OrderTableViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableQueryService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableQueryService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public List<OrderTableViewResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableViewResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableViewResponse findById(Long id) {
        return OrderTableViewResponse.of(orderTableRepository.findById(id)
                .orElseThrow(EntityNotExistsException::new)
        );
    }
}
