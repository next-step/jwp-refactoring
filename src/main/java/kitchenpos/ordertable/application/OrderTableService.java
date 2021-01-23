package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
	private final OrdersRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderTableService(final OrdersRepository orderRepository, final OrderTableRepository orderTableRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest request) {
		OrderTable persistOrderTable = orderTableRepository.save(request.toEntity());
		return OrderTableResponse.of(persistOrderTable);
	}

	public List<OrderTableResponse> list() {
		return orderTableRepository.findAll().stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);
		Orders order = orderRepository.findByOrderTable(savedOrderTable);
		order.validateStatus();
		savedOrderTable.changeEmptyState(request.isEmpty());
		return OrderTableResponse.of(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);
		savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.of(savedOrderTable);
	}

	public List<OrderTable> findAllById(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	public OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);
	}

	public List<OrderTable> findAllByTableGroup(TableGroup tableGroup) {
		return orderTableRepository.findAllByTableGroup(tableGroup);
	}
}
