package kitchenpos.order.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderQueryService {

	private final OrderRepository orderRepository;

	public OrderQueryService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Orders> findAllByOrderTable(OrderTable orderTable) {
		return orderRepository.findAllByOrderTable(orderTable);
	}

	public List<Orders> findAll() {
		return orderRepository.findAll();
	}

	public Orders findById(Long orderId) {
		return orderRepository.findById(orderId)
			  .orElseThrow(EntityNotFoundException::new);
	}
}
