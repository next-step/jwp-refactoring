package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
		OrderTable orderTable = orderTableRepository.save(orderTableRequest.toOrderTable());

        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
    	List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
			.map(orderTable -> OrderTableResponse.of(orderTable))
			.collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTable) {
    	final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
		validateTableGrouped(savedOrderTable);
		validateCookingOrMealStatusExists(orderTableId);
		savedOrderTable.updateEmpty(orderTable.isEmpty());
		OrderTable updatedOrderTable = orderTableRepository.save(savedOrderTable);

		return OrderTableResponse.of(updatedOrderTable);
	}

	private void validateCookingOrMealStatusExists(Long orderTableId) {
    	List<Order> orders = orderRepository.findByOrderTableId(orderTableId);
    	if(orders.stream().filter(order -> order.getOrderStatus().equals(OrderStatus.COOKING.name()) || order.getOrderStatus().equals(OrderStatus.MEAL.name())).findAny().isPresent()) {
			throw new IllegalArgumentException();
		}
	}

	private void validateTableGrouped(OrderTable savedOrderTable) {
		if(savedOrderTable.isTableGrouped()) {
			throw new IllegalArgumentException();
		}
	}

	@Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
		validateOrderTableEmpty(savedOrderTable);
		savedOrderTable.updateNumberOfGuests(new NumberOfGuests(orderTableRequest.getNumberOfGuests()));

		return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
	}

	private void validateOrderTableEmpty(OrderTable savedOrderTable) {
		if (savedOrderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}
	}
}
