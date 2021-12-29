package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.dto.OrderTableDto;
import kitchenpos.ordertable.dto.OrderTableRequest;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
	private final OrderTableRepository orderTableRepository;
	private final OrderTableValidator orderTableValidator;

	public OrderTableService(
		OrderTableRepository orderTableRepository,
		OrderTableValidator orderTableValidator
	) {
		this.orderTableRepository = orderTableRepository;
		this.orderTableValidator = orderTableValidator;
	}

	@Transactional
	public OrderTableDto create(OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.save(request.toOrderTable());
		return OrderTableDto.from(orderTable);
	}

	public List<OrderTableDto> list() {
		List<OrderTable> orderTables = orderTableRepository.findAll();
		return orderTables.stream()
			.map(OrderTableDto::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableDto changeEmpty(Long id, OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		orderTable.changeEmpty(request.isEmpty(), orderTableValidator);
		return OrderTableDto.from(orderTable);
	}

	@Transactional
	public OrderTableDto changeNumberOfGuests(Long id, OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		NumberOfGuests numberOfGuests = NumberOfGuests.from(request.getNumberOfGuests());
		orderTable.changeNumberOfGuests(numberOfGuests);
		return OrderTableDto.from(orderTable);
	}
}
