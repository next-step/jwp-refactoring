package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.AlreadyJoinedTableGroupException;

@Service
public class TableService {
	private final OrderTableRepository orderTableRepository;

	public TableService(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTable create(OrderTable orderTable) {
		return orderTableRepository.save(orderTable);
	}

	public List<OrderTable> findAll() {
		return orderTableRepository.findAll();
	}

	@Transactional
	public OrderTable changeEmpty(Long orderTableId, OrderTable orderTable) {
		OrderTable savedOrderTable = findById(orderTableId);

		if (savedOrderTable.hasTableGroup()) {
			throw new AlreadyJoinedTableGroupException();
		}

		// TODO validate
		// savedOrderTable.validateChangeEmpty();

		savedOrderTable.changeEmpty(orderTable.isEmpty());

		return orderTableRepository.save(savedOrderTable);
	}

	@Transactional
	public OrderTable changeNumberOfGuests(Long orderTableId, OrderTable orderTable) {
		OrderTable savedOrderTable = findById(orderTableId);

		savedOrderTable.changeNumberOfGuests(orderTable);

		return savedOrderTable;
	}

	public OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
								   .orElseThrow(EntityNotFoundException::new);
	}
}
