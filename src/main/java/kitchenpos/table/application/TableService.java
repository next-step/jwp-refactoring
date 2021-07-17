package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableRequest;
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
        // orderTableRequest.setTableGroupId(null);
		OrderTable orderTable = orderTableRepository.save(orderTableRequest.toOrderTable());

        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
    	List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
			.map(orderTable -> OrderTableResponse.of(orderTable))
			.collect(Collectors.toList());
    	//return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTable) {
       final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
       if(savedOrderTable.isTableGrouped()) {
		   throw new IllegalArgumentException();
	   }

		if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.updateEmpty(orderTable.isEmpty());
		OrderTable updatedOrderTable = orderTableRepository.save(savedOrderTable);
		return OrderTableResponse.of(updatedOrderTable);
/*
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
*/
		//		savedOrderTable.setEmpty(orderTable.isEmpty());
	}

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);

		if (savedOrderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}

		savedOrderTable.updateNumberOfGuests(orderTableRequest.getNumberOfGuests());

		return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
/*
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
		final NumberOfGuests numberOfGuests1 = orderTableRequest.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

*/
		//savedOrderTable.setNumberOfGuests(numberOfGuests);
	}
}
