package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.table.domain.EmptyTableValidator;
import kitchenpos.table.domain.NumberOfGuestsValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
public class TableService {
	private final OrderTableRepository orderTableRepository;
	private final EmptyTableValidator emptyTableValidator;
	private final NumberOfGuestsValidator numberOfGuestsValidator;

	public TableService(OrderTableRepository orderTableRepository,
						EmptyTableValidator emptyTableValidator,
						NumberOfGuestsValidator numberOfGuestsValidator) {
		this.orderTableRepository = orderTableRepository;
		this.emptyTableValidator = emptyTableValidator;
		this.numberOfGuestsValidator = numberOfGuestsValidator;
	}

	@Transactional
	public OrderTable create(OrderTable orderTable) {
		return orderTableRepository.save(orderTable);
	}

	public List<OrderTable> findAll() {
		return orderTableRepository.findAll();
	}

	@Transactional
	public OrderTable changeEmpty(Long orderTableId, Boolean empty) {
		OrderTable orderTable = findById(orderTableId);

		emptyTableValidator.validate(orderTable);
		orderTable.changeEmpty(empty);

		return orderTableRepository.save(orderTable);
	}

	@Transactional
	public OrderTable changeNumberOfGuests(Long orderTableId, OrderTable orderTable) {
		OrderTable savedOrderTable = findById(orderTableId);

		savedOrderTable.changeNumberOfGuests(orderTable, numberOfGuestsValidator);

		return savedOrderTable;
	}

	public OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
								   .orElseThrow(EntityNotFoundException::new);
	}
}
