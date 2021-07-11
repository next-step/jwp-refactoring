package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class TableService {
	private final OrderTableRepository orderTableRepository;

	public TableService(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
		NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableRequest.getNumberOfGuests());
		OrderTable orderTable = new OrderTable(null, numberOfGuests,
			orderTableRequest.isEmpty());
		return OrderTableResponse.of(orderTableRepository.save(orderTable));
	}

	public List<OrderTableResponse> list() {
		return orderTableRepository.findAll().stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = findOrderTable(orderTableId);
		savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
		return OrderTableResponse.of(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		OrderTable orderTable = findOrderTable(orderTableId);

		NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableRequest.getNumberOfGuests());
		orderTable.changeNumberOfGuests(numberOfGuests);
		return OrderTableResponse.of(orderTableRepository.save(orderTable));
	}

	private OrderTable findOrderTable(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 주문 테이블을 찾을 수 없습니다."));
	}
}
