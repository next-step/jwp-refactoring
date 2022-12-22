package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.ui.request.NumberOfGuestsRequest;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.request.TableStatusRequest;
import kitchenpos.table.ui.response.OrderTableResponse;

@Service
public class TableService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public TableService(
		final OrderRepository orderRepository,
		final OrderTableRepository orderTableRepository
	) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest request) {
		return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
	}

	@Transactional(readOnly = true)
	public List<OrderTableResponse> list() {
		return OrderTableResponse.listFrom(orderTableRepository.findAll());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final TableStatusRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);
		validate(orderTableId);
		savedOrderTable.updateEmpty(request.isEmpty());
		return OrderTableResponse.from(savedOrderTable);
	}

	private void validate(Long orderTableId) {
		if (isCookingOrMeal(orderTableId)) {
			throw new IllegalArgumentException("조리중이거나 식사중인 테이블은 빈 테이블로 변경할 수 없습니다.");
		}
	}

	private boolean isCookingOrMeal(Long orderTableId) {
		return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, cookingAndMealStatus());
	}

	private List<OrderStatus> cookingAndMealStatus() {
		return Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
		final OrderTable savedOrderTable = findById(orderTableId);
		savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.from(savedOrderTable);
	}

	@Transactional(readOnly = true)
	public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	@Transactional(readOnly = true)
	public Optional<OrderTable> findById(long orderTableId) {
		return orderTableRepository.findById(orderTableId);
	}

	private OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new NotFoundException(String.format("주문 테이블 id(%d)를 찾을 수 없습니다.", orderTableId)));
	}

}
