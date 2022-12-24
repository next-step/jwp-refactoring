package kitchenpos.table.table.appliation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.table.domain.OrderTableRepository;
import kitchenpos.table.table.ui.request.NumberOfGuestsRequest;
import kitchenpos.table.table.ui.request.OrderTableRequest;
import kitchenpos.table.table.ui.request.TableStatusRequest;
import kitchenpos.table.table.ui.response.OrderTableResponse;

@Service
public class TableService {
	private final OrderTableRepository orderTableRepository;
	private final TableValidator tableValidator;

	public TableService(
		final OrderTableRepository orderTableRepository,
		final TableValidator tableValidator
	) {
		this.orderTableRepository = orderTableRepository;
		this.tableValidator = tableValidator;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest request) {
		return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
	}

	@Transactional(readOnly = true)
	public List<OrderTableResponse> list() {
		return OrderTableResponse.listFrom(orderTableRepository.findAll());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final TableStatusRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);
		tableValidator.validateChangeEmpty(orderTableId);
		savedOrderTable.updateEmpty(request.isEmpty());
		return OrderTableResponse.from(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);
		savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.from(savedOrderTable);
	}

	@Transactional(readOnly = true)
	public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	@Transactional(readOnly = true)
	public Optional<OrderTable> findById(long orderTableId) {
		return orderTableRepository.findById(orderTableId);
	}

	private OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new NotFoundException(String.format("주문 테이블 id(%d)를 찾을 수 없습니다.", orderTableId)));
	}

}
