package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@Service
public class TableService {
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;

	public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderTable create(final OrderTable orderTable) {
		orderTable.setTableGroupId(null);

		return orderTableDao.save(orderTable);
	}

	public List<OrderTable> list() {
		return orderTableDao.findAll();
	}

	@Transactional
	public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 주문 테이블을 찾을 수 없습니다."));

		if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
			throw new IllegalArgumentException("단체 지정되어있는 테이블은 변경할 수 없습니다.");
		}

		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException("주문 테이블의 주문상태가 완료되지 않아 변경할 수 없습니다");
		}

		savedOrderTable.setEmpty(orderTable.isEmpty());

		return orderTableDao.save(savedOrderTable);
	}

	@Transactional
	public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
		final int numberOfGuests = orderTable.getNumberOfGuests();

		if (numberOfGuests < 0) {
			throw new IllegalArgumentException("방문 손님 수는 0보다 작을 수 없습니다.");
		}

		final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 주문 테이블을 찾을 수 없습니다."));

		if (savedOrderTable.isEmpty()) {
			throw new IllegalArgumentException("비어있는 테이블입니다.");
		}

		savedOrderTable.setNumberOfGuests(numberOfGuests);

		return orderTableDao.save(savedOrderTable);
	}
}
