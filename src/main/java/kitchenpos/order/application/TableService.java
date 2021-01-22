package kitchenpos.order.application;

import kitchenpos.common.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableDao;
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
	private final OrderTableDao orderTableDao;

	public TableService(final OrderTableDao orderTableDao) {
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest_Create request) {
		OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
		return OrderTableResponse.of(orderTableDao.save(orderTable));
	}

	public List<OrderTableResponse> list() {
		return orderTableDao.findAll().stream()
				.map(OrderTableResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(long orderTableId, OrderTableRequest_ChangeEmpty request) {
		OrderTable orderTable = orderTableDao.findById(orderTableId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));
		orderTable.changeEmpty(request.isEmpty());
		return OrderTableResponse.of(orderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, OrderTableRequest_ChangeGuests request) {
		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));

		savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
	}
}
