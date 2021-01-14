package kitchenpos.ordertable.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderTableService {

	private final OrderDao orderDao;
	private final OrderTableRepository orderTableRepository;

	public OrderTableService(OrderDao orderDao, OrderTableRepository orderTableRepository) {
		this.orderDao = orderDao;
		this.orderTableRepository = orderTableRepository;
	}

	public OrderTableResponse create(final OrderTableRequest request) {
		OrderTable savedOrderTable = orderTableRepository.save(request.newEntity());
		return OrderTableResponse.of(savedOrderTable);
	}

	public List<OrderTableResponse> list() {
		return orderTableRepository.findAll().stream()
			  .map(OrderTableResponse::of)
			  .collect(Collectors.toList());
	}

	public OrderTableResponse changeEmpty(final Long orderTableId,
		  final OrderTableRequest request) {
		OrderTable savedOrderTable = findById(orderTableId);

		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
			  orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.changeEmpty(request.isEmpty());
		return OrderTableResponse.of(savedOrderTable);
	}

	public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
		  final OrderTableRequest request) {
		OrderTable orderTable = findById(orderTableId);
		orderTable.changeNumberOfGuests(request.getNumberOfGuests());

		return OrderTableResponse.of(orderTable);
	}

	public OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			  .orElseThrow(IllegalArgumentException::new);
	}

	public List<OrderTable> findAllByOrderTableIds(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
		return orderTableRepository.findAllByTableGroupId(tableGroupId);
	}
}
