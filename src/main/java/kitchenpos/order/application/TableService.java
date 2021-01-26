package kitchenpos.order.application;

import common.application.NotFoundException;
import domain.order.OrderTable;
import domain.order.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest_ChangeEmpty;
import kitchenpos.order.dto.OrderTableRequest_ChangeGuests;
import kitchenpos.order.dto.OrderTableRequest_Create;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
	static final String MSG_CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by orderTableId";
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest_Create request) {
		OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
		return OrderTableResponse.of(orderTableRepository.save(orderTable));
	}

	public List<OrderTableResponse> list() {
		return orderTableRepository.findAll().stream()
				.map(OrderTableResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(long orderTableId, OrderTableRequest_ChangeEmpty request) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));
		orderTable.changeEmpty(request.isEmpty());
		return OrderTableResponse.of(orderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, OrderTableRequest_ChangeGuests request) {
		final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));

		savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
	}
}
