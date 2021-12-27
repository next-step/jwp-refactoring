package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.OrderTables;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableService {
	private final OrderTableRepository orderTableRepository;

	public OrderTableService(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTable create(final OrderTableRequest orderTableRequest) {
		OrderTable orderTable = orderTableRequest.toOrderTable();
		return orderTableRepository.save(orderTable);
	}

	@Transactional(readOnly = true)
	public List<OrderTable> list() {
		return orderTableRepository.findAll();
	}

	@Transactional
	public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		OrderTable orderTable = findById(orderTableId);
		orderTable.changeStatus(orderTableRequest.isEmpty());
		return orderTable;
	}

	@Transactional
	public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		validateNumberOfGuestsLessThanZero(orderTableRequest);
		OrderTable orderTable = findById(orderTableId);
		orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
		return orderTable;
	}

	private void validateNumberOfGuestsLessThanZero(OrderTableRequest orderTableRequest) {
		if (orderTableRequest.getNumberOfGuests() < 0) {
			throw new IllegalArgumentException("인원수는 0보다 작을 수 없습니다");
		}
	}

	@Transactional(readOnly = true)
	public OrderTables findOrderTableByIdIn(List<Long> orderTableIds) {
		return new OrderTables(orderTableRepository.findAllById(orderTableIds));
	}

	@Transactional(readOnly = true)
	public OrderTable findById(long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다"));
	}
}
