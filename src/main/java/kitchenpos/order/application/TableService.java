package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.exception.OrderException;

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
