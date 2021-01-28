package kitchenpos.table.application;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Service
@Transactional
public class TableService {
	private final OrderTableDao orderTableDao;

	public TableService(final OrderTableDao orderTableDao) {
		this.orderTableDao = orderTableDao;
	}

	public OrderTableResponse create(final OrderTableRequest request) {
		return OrderTableResponse.from(orderTableDao.save(request.toOrderTable()));
	}

	@Transactional(readOnly = true)
	public List<OrderTableResponse> list() {
		return OrderTableResponse.newList(orderTableDao.findAllBy());
	}

	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);

		savedOrderTable.changeEmpty(request.isEmpty());

		return OrderTableResponse.from(savedOrderTable);
	}

	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);

		savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

		return OrderTableResponse.from(savedOrderTable);
	}

	public OrderTable findById(final Long id) {
		return orderTableDao.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 OrderTable이 존재하지 않습니다."));
	}

	public Set<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
		return orderTableDao.findAllByTableGroupId(tableGroupId);
	}
}
