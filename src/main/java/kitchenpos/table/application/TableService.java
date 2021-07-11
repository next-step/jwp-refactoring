package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class TableService {
	private final OrderTableRepository orderTableRepository;
	private final OrderRepository orderRepository;

	public TableService(OrderTableRepository orderTableRepository,
		OrderRepository orderRepository) {
		this.orderTableRepository = orderTableRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
		NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableRequest.getNumberOfGuests());
		OrderTable orderTable = new OrderTable(null, numberOfGuests,
			orderTableRequest.isEmpty());
		return OrderTableResponse.of(orderTableRepository.save(orderTable));
	}

	public List<OrderTableResponse> list() {
		return orderTableRepository.findAll().stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = findOrderTable(orderTableId);

		List<Order> orders = orderRepository.findByOrderTableId(orderTableId);
		boolean unChangeable = orders.stream().anyMatch(Order::isUnChangeable);
		if (unChangeable) {
			throw new IllegalArgumentException("주문 상태가 완료되어야 단체지정이 해제가능합니다.");
		}

		savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
		return OrderTableResponse.of(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		OrderTable orderTable = findOrderTable(orderTableId);

		NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableRequest.getNumberOfGuests());
		orderTable.changeNumberOfGuests(numberOfGuests);
		return OrderTableResponse.of(orderTableRepository.save(orderTable));
	}

	private OrderTable findOrderTable(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 주문 테이블을 찾을 수 없습니다."));
	}
}
