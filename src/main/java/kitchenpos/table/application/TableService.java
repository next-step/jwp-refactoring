package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Service
public class TableService {
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;

	public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest request) {
		OrderTable savedOrderTable = orderTableDao.save(new OrderTable.Builder()
			.numberOfGuests(request.getNumberOfGuests())
			.empty(request.isEmpty())
			.build());

		return OrderTableResponse.from(savedOrderTable);
	}

	public List<OrderTableResponse> list() {
		return OrderTableResponse.newList(orderTableDao.findAll());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);

		if (Objects.nonNull(savedOrderTable.getTableGroup())) {
			throw new IllegalArgumentException();
		}

		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.setEmpty(request.isEmpty());

		return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
		final int numberOfGuests = request.getNumberOfGuests();

		if (numberOfGuests < 0) {
			throw new IllegalArgumentException();
		}

		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);

		if (savedOrderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.setNumberOfGuests(numberOfGuests);

		return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
	}
}
