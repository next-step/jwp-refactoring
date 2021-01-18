package kitchenpos.order.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableQueryService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

	private final OrderQueryService orderQueryService;
	private final OrderTableQueryService orderTableQueryService;
	private final MenuService menuService;

	public OrderService(
		  OrderQueryService orderQueryService,
		  OrderTableQueryService orderTableQueryService,
		  MenuService menuService) {
		this.orderQueryService = orderQueryService;
		this.orderTableQueryService = orderTableQueryService;
		this.menuService = menuService;
	}

	@Transactional
	public OrderResponse create(final OrderRequest request) {
		Set<Long> menuIds = request.menuIds();
		List<Menu> menus = menuService.findAllByIds(menuIds);
		OrderTable orderTable = orderTableQueryService.findById(request.getOrderTableId());

		Orders savedOrder = orderQueryService.save(request.toOrderEntity(orderTable));
		List<OrderLineItem> orderLineItems = orderQueryService
			  .saveOrderLineItems(request.toOrderLineItems(savedOrder, menus));
		return OrderResponse.of(orderLineItems);
	}

	public List<OrderResponse> list() {
		List<Orders> orders = orderQueryService.findAll();
		return orders.stream()
			  .map(order -> {
				  List<OrderLineItem> orderLineItems = orderQueryService
						.findAllOrderLineItems(order);
				  if (CollectionUtils.isEmpty(orderLineItems)) {
					  return OrderResponse.of(order);
				  }

				  return OrderResponse.of(orderLineItems);
			  })
			  .collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
		Orders order = orderQueryService.findById(orderId);
		order.changeStatus(request.getOrderStatus());
		return OrderResponse.of(order);
	}
}
