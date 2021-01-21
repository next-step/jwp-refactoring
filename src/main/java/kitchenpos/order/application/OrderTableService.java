package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderTableService {
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final TableGroupRepository tableGroupRepository;

	public OrderTableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.tableGroupRepository = tableGroupRepository;
	}

	@Transactional
	public OrderTableResponse create(final OrderTableRequest request) {
		return OrderTableResponse.of(orderTableRepository.save(request.toEntity()));
	}

	public List<OrderTableResponse> listTables() {
		return OrderTableResponse.of(orderTableRepository.findAll());
	}

	@Transactional
	public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new IllegalArgumentException());

		if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException("계산완료된 테이블인 경우에만 상태를 변경할 수 있습니다.");
		}
		orderTable.changeEmpty(request.isEmpty());
		return OrderTableResponse.of(orderTable);
	}

	@Transactional
	public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new IllegalArgumentException());
		orderTable.changeNumberOfGuests(request.getNumberOfGuests());
		return OrderTableResponse.of(orderTable);
	}

	public void unGroup(TableGroup tableGroup) {
		List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);

		orderTables.forEach(orderTable -> {
			List<Orders> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
			OrderList orderList = new OrderList(orders);
			orderTable.unGroup(orderList.isCompleteAllOrders());
		});
	}
}
