package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.InvalidNumberOfGuestsException;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	OrderTableRepository orderTableRepository;

	@InjectMocks
	TableService tableService;

	@Test
	@DisplayName("주문 테이블 생성")
	void testCreateTable() {
		OrderTable2 orderTable = createOrderTable();
		when(orderTableRepository.save(orderTable)).thenAnswer(returnsFirstArg());

		OrderTable2 createdOrderTable = tableService.create(orderTable);

		verify(orderTableRepository, times(1)).save(orderTable);
		assertThat(createdOrderTable).isEqualTo(orderTable);
	}

	@Test
	@DisplayName("주문 테이블 목록 조회")
	void testListOrderTable() {
		List<OrderTable2> orderTables = createOrderTables();
		when(orderTableRepository.findAll()).thenReturn(orderTables);

		List<OrderTable2> savedOrderTables = tableService.findAll();

		verify(orderTableRepository, times(1)).findAll();
		assertThat(savedOrderTables).containsExactlyElementsOf(orderTables);
	}

	@Test
	@DisplayName("빈 테이블로 변경")
	void changeEmpty() {
		OrderTable2 previousOrderTable = createOrderTable(1L, false);
		OrderTable2 emptyOrderTable = createOrderTable(2L, true);

		// TODO check meal is complete
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(previousOrderTable));
		when(orderTableRepository.save(any())).thenAnswer(returnsFirstArg());

		OrderTable2 actualOrderTable = tableService.changeEmpty(emptyOrderTable.getId(), emptyOrderTable);

		verify(orderTableRepository, times(1)).save(previousOrderTable);
		assertThat(actualOrderTable.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("주문 테이블의 손님 수 변경")
	void testChangeNumberOfGuests() {
		long orderTableId = 1L;
		OrderTable2 previousOrderTable = createOrderTable(orderTableId, true);
		OrderTable2 expectedOrderTable = createOrderTable(orderTableId, 10, true);

		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(previousOrderTable));

		OrderTable2 actualOrderTable = tableService.changeNumberOfGuests(orderTableId, expectedOrderTable);

		assertThat(actualOrderTable.getNumberOfGuests()).isEqualTo(expectedOrderTable.getNumberOfGuests());
	}

	@Test
	@DisplayName("주문 테이블의 손님 수를 0 미만으로 변경")
	void testChangeNumberOfGuestsBelowThanZero() {
		long orderTableId = 1L;
		OrderTable2 previousOrderTable = createOrderTable(orderTableId, true);
		OrderTable2 expectedOrderTable = createOrderTable(orderTableId, -1, true);

		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(previousOrderTable));

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, expectedOrderTable))
			.isInstanceOf(InvalidNumberOfGuestsException.class);
	}

	@Test
	@DisplayName("주문 테이블의 손님 수 변경시 주문 테이블이 존재하지 않음")
	void testChangeNumberOfGuestsWithNotExistsTableId() {
		long orderTableId = 1L;
		OrderTable2 expectedOrderTable = createOrderTable(orderTableId, -1, true);

		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, expectedOrderTable))
			.isInstanceOf(EntityNotFoundException.class);
	}

	// TODO
	@Test
	@DisplayName("주문 테이블의 손님 수 변경시 주문이 완료되지 않음")
	void testChangeEmptyWhenOrderStatusNotCompletion() {
	}

	private List<OrderTable2> createOrderTables() {
		return Lists.newArrayList(
			createOrderTable(1L, false),
			createOrderTable(2L, false),
			createOrderTable(3L, false)
		);
	}

	private OrderTable2 createOrderTable(long id, int numberOfGuests, boolean empty) {
		return new OrderTable2(id,numberOfGuests, empty);
	}

	private OrderTable2 createOrderTable(long id, boolean empty) {
		return new OrderTable2(id,1, empty);
	}

	private OrderTable2 createOrderTable() {
		return createOrderTable(1L, false);
	}
}
