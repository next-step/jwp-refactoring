package kitchenpos.ordertable.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableRepository;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

@Service
public class TableService {
	private final TableRepository tableRepository;
	private final OrderRepository orderRepository;

	public TableService(final TableRepository tableRepository, final OrderRepository orderRepository) {
		this.tableRepository = tableRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
		OrderTable saved = tableRepository.save(orderTableRequest.toOrderTable());
		return OrderTableResponse.of(saved);
	}

	public List<OrderTableResponse> list() {
		return OrderTableResponse.of(tableRepository.findAll());
	}

	@Transactional
	public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
		final OrderTable savedOrderTable = tableRepository.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);
		final Order savedOrder = orderRepository.findByOrderTableId(orderTableId);
		savedOrderTable.changeEmpty(orderTableChangeEmptyRequest.isEmpty(), savedOrder);
		return tableRepository.save(savedOrderTable);
	}

	@Transactional
	public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
		final OrderTable savedOrderTable = tableRepository.findById(orderTableId)
			.orElseThrow(IllegalArgumentException::new);
		savedOrderTable.changeNumberOfGuests(new NumberOfGuests(orderTableRequest.getNumberOfGuests()));

		return tableRepository.save(savedOrderTable);
	}
}
