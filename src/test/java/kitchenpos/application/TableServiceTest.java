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

import kitchenpos.domain.OrderTable;
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
	@DisplayName("주문 테이블 생성 성공")
	void testCreateTable() {
		OrderTable orderTable = createOrderTable();
		when(orderTableRepository.save(orderTable)).thenAnswer(returnsFirstArg());

		OrderTable createdOrderTable = tableService.create(orderTable);

		verify(orderTableRepository, times(1)).save(orderTable);
		assertThat(createdOrderTable).isEqualTo(orderTable);
	}

	@Test
	@DisplayName("주문 테이블 목록 조회 성공")
	void testListOrderTable() {
		List<OrderTable> orderTables = createOrderTables();
		when(orderTableRepository.findAll()).thenReturn(orderTables);

		List<OrderTable> savedOrderTables = tableService.findAll();

		verify(orderTableRepository, times(1)).findAll();
		assertThat(savedOrderTables).containsExactlyElementsOf(orderTables);
	}

	@Test
	@DisplayName("빈 테이블로 변경 성공")
	void changeEmpty() {
		OrderTable previousOrderTable = createOrderTable(1L, false);
		OrderTable emptyOrderTable = createOrderTable(2L, true);

		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(previousOrderTable));
		when(orderTableRepository.save(any())).thenAnswer(returnsFirstArg());

		OrderTable actualOrderTable = tableService.changeEmpty(emptyOrderTable.getId(), emptyOrderTable);

		verify(orderTableRepository, times(1)).save(previousOrderTable);
		assertThat(actualOrderTable.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("주문 테이블의 손님 수 변경 성공")
	void testChangeNumberOfGuests() {
		long orderTableId = 1L;
		OrderTable previousOrderTable = createOrderTable(orderTableId, true);
		OrderTable expectedOrderTable = createOrderTable(orderTableId, 10, true);

		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(previousOrderTable));

		OrderTable actualOrderTable = tableService.changeNumberOfGuests(orderTableId, expectedOrderTable);

		assertThat(actualOrderTable.getNumberOfGuests()).isEqualTo(expectedOrderTable.getNumberOfGuests());
	}

	@Test
	@DisplayName("주문 테이블의 손님 수를 0 미만으로 변경시 실패")
	void testChangeNumberOfGuestsBelowThanZero() {
		long orderTableId = 1L;
		OrderTable previousOrderTable = createOrderTable(orderTableId, true);
		OrderTable expectedOrderTable = createOrderTable(orderTableId, -1, true);

		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(previousOrderTable));

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, expectedOrderTable))
			.isInstanceOf(InvalidNumberOfGuestsException.class);
	}

	@Test
	@DisplayName("주문 테이블의 손님 수 변경시 주문 테이블이 존재하지 않음")
	void testChangeNumberOfGuestsWithNotExistsTableId() {
		long orderTableId = 1L;
		OrderTable expectedOrderTable = createOrderTable(orderTableId, -1, true);

		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, expectedOrderTable))
			.isInstanceOf(EntityNotFoundException.class);
	}

	private List<OrderTable> createOrderTables() {
		return Lists.newArrayList(
			createOrderTable(1L, false),
			createOrderTable(2L, false),
			createOrderTable(3L, false)
		);
	}

	private OrderTable createOrderTable(long id, int numberOfGuests, boolean empty) {
		return new OrderTable(id, numberOfGuests, empty);
	}

	private OrderTable createOrderTable(long id, boolean empty) {
		return new OrderTable(id, 1, empty);
	}

	private OrderTable createOrderTable() {
		return createOrderTable(1L, false);
	}
}
