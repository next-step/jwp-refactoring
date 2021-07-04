package kitchenpos.application.query;

import kitchenpos.dto.response.OrderViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {
    private final OrderRepository orderRepository;

    public OrderQueryService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
