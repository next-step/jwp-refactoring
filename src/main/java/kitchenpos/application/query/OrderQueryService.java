package kitchenpos.application.query;

import kitchenpos.dto.response.OrderViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderQueryService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public List<OrderViewResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderViewResponse::of)
                .collect(Collectors.toList());
    }

    public OrderViewResponse findById(Long id) {
        return OrderViewResponse.of(orderRepository.findById(id)
                .orElseThrow(EntityNotExistsException::new));
    }
}
