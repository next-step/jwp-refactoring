package kitchenpos.application;

import kitchenpos.common.NotFoundException;
import kitchenpos.common.TableValidationException;
import kitchenpos.common.ValidationException;
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
	static final String MSG_CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by orderTableId";
	static final String MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP = "Cannot change empty of already in TableGroup";
	static final String MSG_CANNOT_CHANGE_EMPTY_ORDER_ONGOING = "Cannot change empty while Order is ongoing";
	static final String MSG_CANNOT_CHANGE_QUEST_WHILE_EMPTY = "Cannot change guests while OrderTable is empty";
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
		return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
	}

	public List<OrderTableResponse> list() {
		return orderTableDao.findAll().stream()
				.map(OrderTableResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, OrderTableRequest_ChangeEmpty request) {
		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));

		if (Objects.nonNull(savedOrderTable.getTableGroup())) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP);
		}

		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
				orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_EMPTY_ORDER_ONGOING);
		}

		savedOrderTable.changeEmpty(request.isEmpty());

		return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, OrderTableRequest_ChangeGuests request) {
		// TODO: 2021-01-19 Guest 객체화
		final int numberOfGuests = request.getNumberOfGuests();
		if (numberOfGuests < 0) {
			throw new ValidationException("numberOfGuests must be equal or greater than zero");
		}

		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));

		if (savedOrderTable.isEmpty()) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_QUEST_WHILE_EMPTY);
		}

		savedOrderTable.changeNumberOfGuests(numberOfGuests);

		return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
	}
}
