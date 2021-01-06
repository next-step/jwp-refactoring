package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.AlreadyOrderException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class TableService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create() {
		OrderTable orderTable = orderTableRepository.save(OrderTable.create());
		return OrderTableResponse.of(orderTable);
	}

	public List<OrderTableResponse> list() {
		List<OrderTable> orderTables = orderTableRepository.findAll();
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = getOrderTableById(orderTableId);

		if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new AlreadyOrderException("이미 주문 진행 상태입니다.");
		}

		savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

		return OrderTableResponse.of(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {

		final OrderTable savedOrderTable = getOrderTableById(orderTableId);

		savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());

		return OrderTableResponse.of(savedOrderTable);
	}

	private OrderTable getOrderTableById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new NotFoundException("주문 테이블 정보가 없습니다."));
	}
}
