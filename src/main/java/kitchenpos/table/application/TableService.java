package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
	private final OrderService orderService;
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderService orderService, final OrderTableRepository orderTableRepository) {
		this.orderService = orderService;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
		return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toOrderTable()));
	}

	public List<OrderTableResponse> list() {
		return OrderTableResponse.of(orderTableRepository.findAll());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
		final OrderTable orderTable = findById(orderTableId);

		orderService.checkProcessingOrder(orderTableId);
		orderTable.changeEmpty(empty);
		return OrderTableResponse.of(orderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
		final OrderTable orderTable = findById(orderTableId);
		orderTable.changeNumberOfGuests(numberOfGuests);
		return OrderTableResponse.of(orderTable);
	}

	private OrderTable findById(Long id) {
		return orderTableRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("주문 테이블이 존재하지 않습니다. id: " + id));
	}
}
