package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tableGroup.exception.TableException;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderService(
		final MenuRepository menuRepository,
		final OrderRepository orderRepository,
		final OrderTableRepository orderTableRepository
	) {
		this.menuRepository = menuRepository;
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public Order create(final OrderRequest orderRequest) {
		final OrderTable orderTable = orderTableFindById(orderRequest.getOrderTableId());
		final Order savedOrder = Order.of(orderTable, OrderStatus.COOKING);
		savedOrder.addOrderLineItems(savedOrderLineItems(orderRequest.getOrderLineItems(), savedOrder));
		return orderRepository.save(savedOrder);
	}

	public List<Order> list() {
		return orderRepository.findAll();
	}

	@Transactional
	public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
		final Order savedOrder = orderFindById(orderId);
		savedOrder.updateOrderStatus(orderStatusRequest.getOrderStatus());
		return orderRepository.save(savedOrder);
	}

	List<OrderLineItem> savedOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests,
		Order savedOrder) {
		return orderLineItemRequests.stream()
			.map(it -> (OrderLineItem.of(savedOrder, menuFindById(it.getMenuId()), it.getQuantity())))
			.collect(Collectors.toList());
	}

	private OrderTable orderTableFindById(Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(() -> {
				throw new TableException(ErrorCode.ORDER_TABLE_IS_NULL);
			});
	}

	private Menu menuFindById(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> {
				throw new MenuException(ErrorCode.MENU_IS_NULL);
			});
	}

	private Order orderFindById(Long id) {
		return orderRepository.findById(id)
			.orElseThrow(() -> {
				throw new OrderException(ErrorCode.ORDER_IS_NULL);
			});
	}
}
