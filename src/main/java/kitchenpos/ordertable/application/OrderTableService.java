package kitchenpos.ordertable.application;

import kitchenpos.orders.application.OrderService;
import kitchenpos.orders.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

	public List<OrderTableResponse> list() {
		return orderTableRepository.findAll()
			.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		OrderTable orderTable = findById(orderTableId);
		validateOrderTableIsGrouped(orderTable);
		validateOrderIsCompletion(orderTable);
		changeOrderTableStatus(orderTableRequest, orderTable);
		return orderTable;
	}

	private void changeOrderTableStatus(OrderTableRequest orderTableRequest, OrderTable orderTable) {
		if (orderTableRequest.isEmpty()) {
			orderTable.notUse();
			return;
		}
		orderTable.use();
	}

	private void validateOrderTableIsGrouped(OrderTable orderTable) {
		if (orderTable.isGrouped()) {
			throw new IllegalArgumentException("그룹화 된 테이블은 상태를 변경 할 수 없습니다");
		}
	}

	private void validateOrderIsCompletion(OrderTable orderTable) {
		if (!orderTable.isOrderCompletion()) {
			throw new IllegalArgumentException("테이블의 주문이 계산완료 되지 않았습니다");
		}
	}

	@Transactional
	public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		validateNumberOfGuestsLessThanZero(orderTableRequest);
		OrderTable orderTable = findById(orderTableId);
		validateOrderTableIsNotUse(orderTable);
		orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
		return orderTable;
	}

	private void validateOrderTableIsNotUse(OrderTable orderTable) {
		if (orderTable.isNotUse()) {
			throw new IllegalArgumentException("비어 있는 테이블입니다");
		}
	}

	private void validateNumberOfGuestsLessThanZero(OrderTableRequest orderTableRequest) {
		if (isNumberOfGuestsLessThanZero(orderTableRequest)) {
			throw new IllegalArgumentException("인원수는 0보다 작을 수 없습니다");
		}
	}

	private boolean isNumberOfGuestsLessThanZero(OrderTableRequest orderTableRequest) {
		return orderTableRequest.getNumberOfGuests() < 0;
	}

	public List<OrderTable> findOrderTableByIdIn(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	public OrderTable findById(long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다"));
	}
}
