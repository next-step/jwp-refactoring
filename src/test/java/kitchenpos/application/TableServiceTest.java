package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;

@DisplayName("주문테이블 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private TableService tableService;

	@DisplayName("주문테이블을 등록할 수 있다. 등록 시 그룹 설정은 안되어 있어야 한다.")
	@Test
	void createTest() {
		// given
		OrderTable orderTable = mock(OrderTable.class);

		// when
		tableService.create(orderTable);

		// then
		verify(orderTable).ungroup();
		verify(orderTableRepository).save(orderTable);
	}

	@DisplayName("주문테이블 목록을 조회할 수 있다.")
	@Test
	void listTest() {
		// given
		OrderTable orderTable = mock(OrderTable.class);
		when(tableService.list()).thenReturn(asList(orderTable));

		// when
		List<OrderTable> orderTables = tableService.list();

		// then
		assertThat(orderTables).containsExactly(orderTable);
	}

	@DisplayName("등록된 주문테이블만 상태를 바꿀 수 있다.")
	@Test
	void changeEmptyWithUnknownOrderTableTest() {
		// given
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 되지 않은 주문테이블은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("그룹 설정이 되어 있는 주문테이블은 상태를 바꿀 수 없다.")
	@Test
	void changeEmptyWithGroupedOrderTableTest() {
		// given
		OrderTable groupedOrderTable = mock(OrderTable.class);
		when(groupedOrderTable.isGrouped()).thenReturn(true);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(groupedOrderTable));

		// when
		// then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
	}

	@DisplayName("주문테이블의 주문이 조리 상태이거나 식사 상태이면 주문테이블 상태를 바꿀 수 없다.")
	@Test
	void changeEmptyWithCookingOrderTest() {
		// given
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.isGrouped()).thenReturn(false);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable));

		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
			.thenReturn(true);

		// when
		// then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("주문 테이블을 주문 등록 가능상태 or 주문 등록 불가 상태로 바꿀 수 있다.")
	@Test
	void changeEmptyTest() {
		// given
		OrderTable savedOrderTable = mock(OrderTable.class);
		when(savedOrderTable.isGrouped()).thenReturn(false);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

		OrderTable orderTable = mock(OrderTable.class);

		// when
		tableService.changeEmpty(1L, orderTable);

		// then
		verify(savedOrderTable).setEmpty(orderTable.isEmpty());
		verify(orderTableRepository).save(savedOrderTable);
	}

	@DisplayName("방문 손님 수를 음수로 수정할 수 없다.")
	@Test
	void changeNumberOfGuestsNegativeNumberTest() {
		// given
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getNumberOfGuests()).thenReturn(-1);

		// when
		// then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("방문 손님을 음수로 수정할 수 없습니다.");
	}

	@DisplayName("등록된 주문 테이블만 방문 손님 수를 수정할 수 없다.")
	@Test
	void changeNumberOfGuestsUnknownOrderTableTest() {
		// given
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 주문테이블은 방문 손님 수를 수정할 수 없습니다.");
	}

	@DisplayName("빈 테이블의 방문 손님 수는 수정할 수 없습니다.")
	@Test
	void changeNumberOfGuestsEmptyOrderTableTest() {
		// given
		OrderTable emptyOrderTable = mock(OrderTable.class);
		when(emptyOrderTable.isEmpty()).thenReturn(true);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(emptyOrderTable));

		// when
		// then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("빈 테이블은 방문 손님 수를 수정할 수 없습니다.");
	}

	@DisplayName("주문테이블의 방문 손님 수를 수정할 수 있다.")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable savedOrderTable = mock(OrderTable.class);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

		OrderTable orderTable = mock(OrderTable.class);

		// when
		tableService.changeNumberOfGuests(1L, orderTable);

		// then
		verify(savedOrderTable).setNumberOfGuests(orderTable.getNumberOfGuests());
		verify(orderTableRepository).save(savedOrderTable);
	}
}
