package kitchenpos.ordertable.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableAddRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.InvalidOrderTableEmptyException;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;

@Service
public class TableService {

	private static final List<OrderStatus> COOKING_OR_MEAL = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableAddRequest request) {
		final OrderTable orderTable = orderTableRepository.save(request.toEntity());
		return OrderTableResponse.of(orderTable);
	}

	@Transactional(readOnly = true)
	public List<OrderTableResponse> list() {
		final List<OrderTable> orderTables = orderTableRepository.findAll();
		return orderTables.stream().map(OrderTableResponse::of).collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest request) {
		final OrderTable orderTable = findOrderTable(orderTableId);
		validateEmpty(orderTable);
		orderTable.setEmptyIfNotTableGroup(request.isEmpty());
		return OrderTableResponse.of(orderTable);
	}

	private void validateEmpty(final OrderTable orderTable) {
		if (orderRepository.existsByOrderTable_IdAndOrderStatusIn(orderTable.getId(), COOKING_OR_MEAL)) {
			throw new InvalidOrderTableEmptyException("주문 테이블이 '조리' 혹은 식사' 상태면 수정할 수 없습니다.");
		}
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(
		final Long orderTableId, final OrderTableNumberOfGuestsRequest request
	) {
		final OrderTable orderTable = findOrderTable(orderTableId);
		orderTable.setNumberOfGuestsIfNotEmpty(request.getNumberOfGuests());
		return OrderTableResponse.of(orderTable);
	}

	@Transactional(readOnly = true)
	private OrderTable findOrderTable(final Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(NotFoundOrderTableException::new);
	}
}
