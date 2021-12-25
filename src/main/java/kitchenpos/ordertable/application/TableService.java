package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableExternalValidator;
import kitchenpos.ordertable.dto.OrderTableAddRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;

@Service
public class TableService {

	private final OrderTableExternalValidator orderTableExternalValidator;
	private final OrderTableRepository orderTableRepository;

	public TableService(OrderTableExternalValidator orderTableExternalValidator, OrderTableRepository orderTableRepository) {
		this.orderTableExternalValidator = orderTableExternalValidator;
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
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest request) {
		final OrderTable orderTable = findOrderTable(orderTableId);
		orderTable.changeEmpty(orderTableExternalValidator, request.isEmpty());
		return OrderTableResponse.of(orderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(
		final Long orderTableId, final OrderTableNumberOfGuestsRequest request
	) {
		final OrderTable orderTable = findOrderTable(orderTableId);
		orderTable.changeNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.of(orderTable);
	}

	private OrderTable findOrderTable(final Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(NotFoundOrderTableException::new);
	}
}
