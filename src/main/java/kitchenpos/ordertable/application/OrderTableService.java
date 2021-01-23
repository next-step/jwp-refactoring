package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.application.TableGroupService;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
	private final OrderDao orderDao;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupService tableGroupService;

	public OrderTableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository,
		final TableGroupService tableGroupService) {
		this.orderDao = orderDao;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupService = tableGroupService;
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
		/*
		if (orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.setEmpty(orderTable.isEmpty());
		*/
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

	private OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);
	}
}
