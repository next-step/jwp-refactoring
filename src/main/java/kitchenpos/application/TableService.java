package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class TableService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create() {
		OrderTable orderTable = orderTableRepository.save(OrderTable.create());
		return OrderTableResponse.of(orderTable);
	}

	public List<OrderTableResponse> list() {
		List<OrderTable> orderTables = orderTableRepository.findAll();
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);

		if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

		return OrderTableResponse.of(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {

		final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);

		savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());

		return OrderTableResponse.of(savedOrderTable);
	}
}
