package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.ui.request.NumberOfGuestsRequest;
import kitchenpos.order.ui.request.OrderTableRequest;
import kitchenpos.order.ui.request.TableStatusRequest;
import kitchenpos.order.ui.response.OrderTableResponse;

@Service
public class TableService {
	private final OrderService orderService;
	private final OrderTableRepository orderTableRepository;

	public TableService(
		final OrderService orderService,
		OrderTableRepository orderTableRepository
	) {
		this.orderService = orderService;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest request) {
		return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
	}

	public List<OrderTableResponse> list() {
		return OrderTableResponse.listFrom(orderTableRepository.findAll());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final TableStatusRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);

		if (!savedOrderTable.hasTableGroup()) {
			throw new IllegalArgumentException();
		}

		if (orderService.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.updateEmpty(request.isEmpty());
		return OrderTableResponse.from(savedOrderTable);
	}

	private OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
		final int numberOfGuests = request.getNumberOfGuests();

		if (numberOfGuests < 0) {
			throw new IllegalArgumentException();
		}

		final OrderTable savedOrderTable = findById(orderTableId);

		if (savedOrderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.updateNumberOfGuests(numberOfGuests);
		return OrderTableResponse.from(savedOrderTable);
	}

	@Transactional(readOnly = true)
	public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	@Transactional(readOnly = true)
	public Optional<OrderTable> findById(long orderTableId) {
		return orderTableRepository.findById(orderTableId);
	}
}
