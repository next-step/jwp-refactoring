package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

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
	public OrderResponse create(final OrderRequest orderRequest) {
		final List<Long> menuIds = orderRequest.getMenuIds();

		if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
			throw new NotFoundException("요청된 메뉴 정보 중 데이터베이스에 없는 정보가 존재합니다.");
		}

		final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
			.orElseThrow(() -> new NotFoundException("주문테이블 정보를 찾을 수 없습니다."));

		final List<Menu> menus = menuRepository.findAllById(menuIds);
		final Order savedOrder = orderRepository.save(Order.create(orderTable, menus, orderRequest.getQuantities()));
		return OrderResponse.of(savedOrder);
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderRepository.findAll();
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		final Order savedOrder = orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException("주문 정보를 찾을 수 없습니다."));

		savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
		return OrderResponse.of(savedOrder);
	}
}
