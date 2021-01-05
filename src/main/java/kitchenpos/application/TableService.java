package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

@Service
public class TableService {
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;

	public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
		OrderTable orderTable = orderTableDao.save(OrderTable.create());
		return OrderTableResponse.of(orderTable);
	}

	public List<OrderTableResponse> list() {
		List<OrderTable> orderTables = orderTableDao.findAll();
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);

		if (Objects.nonNull(savedOrderTable.getTableGroup())) {
			throw new IllegalArgumentException();
		}

		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.setEmpty(orderTableRequest.isEmpty());

		return OrderTableResponse.of(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final int numberOfGuests = orderTableRequest.getNumberOfGuests();

		if (numberOfGuests < 0) {
			throw new IllegalArgumentException();
		}

		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);

		if (savedOrderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.setNumberOfGuests(numberOfGuests);

		return OrderTableResponse.of(savedOrderTable);
	}
}
