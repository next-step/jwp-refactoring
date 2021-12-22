package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;

@Service
@Transactional(readOnly = true)
public class TableService {
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTable create(final OrderTableRequest orderTableRequest) {
		return orderTableRepository.save(orderTableRequest.toEntity());
	}

	public List<OrderTable> list() {
		return orderTableRepository.findAll();
	}

	@Transactional
	public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = orderTableFindById(orderTableId);
		savedOrderTable.empty(orderTableRequest.getEmpty());
		return orderTableRepository.save(savedOrderTable);
	}

	@Transactional
	public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = orderTableFindById(orderTableId);
		savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuest());
		return orderTableRepository.save(savedOrderTable);
	}

	private OrderTable orderTableFindById(Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(() -> {
				throw new OrderException(ErrorCode.ORDER_TABLE_IS_NULL);
			});
	}
}
