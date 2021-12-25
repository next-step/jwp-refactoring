package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderService;
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

	private final OrderService orderService;
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderService orderService, final OrderTableRepository orderTableRepository) {
		this.orderService = orderService;
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
		orderTable.changeEmptyIfNotTableGroup(request.isEmpty());
		return OrderTableResponse.of(orderTable);
	}

	private void validateEmpty(final OrderTable orderTable) {
		if (orderService.existsOrderStatusCookingOrMeal(orderTable.getId())) {
			throw new InvalidOrderTableEmptyException("주문 테이블이 '조리' 혹은 식사' 상태면 수정할 수 없습니다.");
		}
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(
		final Long orderTableId, final OrderTableNumberOfGuestsRequest request
	) {
		final OrderTable orderTable = findOrderTable(orderTableId);
		orderTable.changeNumberOfGuestsIfNotEmpty(request.getNumberOfGuests());
		return OrderTableResponse.of(orderTable);
	}

	private OrderTable findOrderTable(final Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(NotFoundOrderTableException::new);
	}
}
