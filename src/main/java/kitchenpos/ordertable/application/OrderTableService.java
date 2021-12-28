package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.dto.OrderTableRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableService {
	private final OrderTableRepository orderTableRepository;
	private final OrderTableValidator orderTableValidator;

	public OrderTableService(OrderTableRepository orderTableRepository,
		OrderTableValidator orderTableValidator) {
		this.orderTableRepository = orderTableRepository;
		this.orderTableValidator = orderTableValidator;
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
		orderTableValidator.validateChangeEmpty(orderTable);
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
	public List<OrderTable> findOrderTableByIdIn(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	@Transactional(readOnly = true)
	public OrderTable findById(long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다"));
	}

	@Transactional(readOnly = true)
	public List<OrderTable> findOrderTablesByTableGroupId(Long tableGroupId) {
		return orderTableRepository.findByTableGroupId(tableGroupId);
	}
}
