package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableDto;
import kitchenpos.ordertable.dto.OrderTableRequest;

@Service(value = "ToBeOrderTableService")
@Transactional(readOnly = true)
public class OrderTableService {
	private final OrderTableRepository orderTableRepository;

	public OrderTableService(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderTableDto create(OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.save(request.toToBeOrderTable());
		return OrderTableDto.of(orderTable);
	}

	public List<OrderTableDto> list() {
		List<OrderTable> orderTables = orderTableRepository.findAll();
		return orderTables.stream()
			.map(OrderTableDto::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderTableDto changeEmpty(Long id, OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		// TODO : 주문 테이블에 완료되지 않은 주문이 남아 있는 경우 변경할 수 없음
		orderTable.changeEmpty(request.isEmpty());
		return OrderTableDto.of(orderTable);
	}

	@Transactional
	public OrderTableDto changeNumberOfGuests(Long id, OrderTableRequest request) {
		OrderTable orderTable = orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		NumberOfGuests numberOfGuests = NumberOfGuests.of(request.getNumberOfGuests());
		orderTable.changeNumberOfGuests(numberOfGuests);
		return OrderTableDto.of(orderTable);
	}
}
