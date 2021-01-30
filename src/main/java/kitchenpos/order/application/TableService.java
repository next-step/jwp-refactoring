package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.utils.ValidationUtils;

@Service
public class TableService {
	private final OrderTableRepository orderTableRepository;

	public TableService(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
		return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toOrderTable()));
	}

	public List<OrderTableResponse> list() {
		List<OrderTable> orderTables = orderTableRepository.findAll();
		return OrderTableResponse.of(orderTables);
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable orderTable = findOrderTableById(orderTableId);

		if (orderTable.isGroupTable()) {
			throw new IllegalArgumentException();
		}

		orderTable.changeEmpty(orderTableRequest.isEmpty());

		return OrderTableResponse.of(orderTable);
	}

	@Transactional
	public OrderTableResponse changeGuestsNumber(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable orderTable = findOrderTableById(orderTableId);

		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException("테이블이 비어 있습니다.");
		}

		orderTable.changeGuestsNumber(orderTableRequest.getNumberOfGuests());

		return OrderTableResponse.of(orderTable);
	}

	public OrderTable findOrderTableById(Long id) {
		return orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
	}

	public List<OrderTable> findAllOrderTablesByIds(List<Long> ids) {
		List<OrderTable> orderTables = orderTableRepository.findAllById(ids);

		ValidationUtils.validateListSize(orderTables, ids, "존재하지 않는 테이블이 있습니다.");

		return orderTables;
	}
}
