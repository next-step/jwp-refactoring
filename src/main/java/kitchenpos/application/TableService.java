package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest_ChangeEmpty;
import kitchenpos.dto.OrderTableRequest_ChangeGuests;
import kitchenpos.dto.OrderTableRequest_Create;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;

	public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest_Create request) {
		OrderTable orderTable = createOrderTable(request);
		return OrderTableResponse.of(orderTableDao.save(orderTable));
	}

	private OrderTable createOrderTable(OrderTableRequest_Create request) {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(request.getNumberOfGuests());
		orderTable.setEmpty(request.isEmpty());
		orderTable.setTableGroupId(null);
		return orderTable;
	}

	public List<OrderTableResponse> list() {
		return orderTableDao.findAll().stream()
				.map(OrderTableResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, OrderTableRequest_ChangeEmpty request) {
		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
				.orElseThrow(IllegalArgumentException::new);

		if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
			throw new IllegalArgumentException();
		}

		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
				orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.setEmpty(request.isEmpty());

		return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, OrderTableRequest_ChangeGuests request) {
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

		return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
	}
}
