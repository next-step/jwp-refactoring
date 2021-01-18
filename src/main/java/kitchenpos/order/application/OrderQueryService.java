package kitchenpos.order.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderQueryService {

	private final OrderRepository orderRepository;
	private final OrderLineItemRepository orderLineItemRepository;

	public OrderQueryService(OrderRepository orderRepository,
		  OrderLineItemRepository orderLineItemRepository) {
		this.orderRepository = orderRepository;
		this.orderLineItemRepository = orderLineItemRepository;
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

	public List<OrderLineItem> findAllOrderLineItems(Orders order) {
		return orderLineItemRepository.findAllByOrders(order);
	}

	public Orders save(Orders entity) {
		return orderRepository.save(entity);
	}

	public List<OrderLineItem> saveOrderLineItems(List<OrderLineItem> orderLineItems) {
		return orderLineItemRepository.saveAll(orderLineItems);
	}
}
