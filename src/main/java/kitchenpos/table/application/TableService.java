package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Service
@Transactional(readOnly = true)
public class TableService {
	private final OrderDao orderDao;
	private final OrderTableRepository orderTableRepository;

	public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
		this.orderDao = orderDao;
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
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTable) {
		final OrderTable savedOrderTable = findById(orderTableId);

		/*
		if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
			throw new IllegalArgumentException();
		}*/
		/*
		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.setEmpty(orderTable.isEmpty());
		*/
		return OrderTableResponse.of(savedOrderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);
		savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.of(savedOrderTable);
	}

	private OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);
	}
}
