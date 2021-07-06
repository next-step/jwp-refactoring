package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class TableService {
	private final OrderDao orderDao;
	private final OrderTableRepository orderTableRepository;

	public TableService(OrderDao orderDao, OrderTableRepository orderTableRepository) {
		this.orderDao = orderDao;
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

		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException("주문 테이블의 주문상태가 완료되지 않아 변경할 수 없습니다");
		}

		savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
		return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
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
