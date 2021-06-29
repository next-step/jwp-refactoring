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
import kitchenpos.domain.OrderTable;

@DisplayName("주문테이블 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

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
		verify(orderTable).setTableGroupId(null);
		verify(orderTableDao).save(orderTable);
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
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록된 주문 테이블만 주문 등록 상태로 바꿀 수 있습니다");
	}

	@DisplayName("그룹 설정이 되어 있는 주문테이블은 상태를 바꿀 수 없다.")
	@Test
	void changeEmptyWithGroupedOrderTableTest() {
		// given
		OrderTable groupedOrderTable = mock(OrderTable.class);
		when(groupedOrderTable.getTableGroupId()).thenReturn(1L);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(groupedOrderTable));

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
		when(orderTable.getTableGroupId()).thenReturn(null);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
			.thenReturn(true);

		// when
		// then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블의 주문이 아직 조리 상태 또는 식사 상태 입니다.");
	}

	@DisplayName("주문 테이블을 주문 등록 가능상태 or 주문 등록 불가 상태로 바꿀 수 있다.")
	@Test
	void changeEmptyTest() {
		// given
		OrderTable savedOrderTable = mock(OrderTable.class);
		when(savedOrderTable.getTableGroupId()).thenReturn(null);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

		OrderTable orderTable = mock(OrderTable.class);

		// when
		tableService.changeEmpty(1L, orderTable);

		// then
		verify(savedOrderTable).setEmpty(orderTable.isEmpty());
		verify(orderTableDao).save(savedOrderTable);
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
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, mock(OrderTable.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록된 주문 테이블만 방문 손님 수를 바꿀 수 있습니다");
	}

	@DisplayName("빈 테이블의 방문 손님 수는 수정할 수 없습니다.")
	@Test
	void changeNumberOfGuestsEmptyOrderTableTest() {
		// given
		OrderTable emptyOrderTable = mock(OrderTable.class);
		when(emptyOrderTable.isEmpty()).thenReturn(true);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(emptyOrderTable));

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

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

		OrderTable orderTable = mock(OrderTable.class);

		// when
		tableService.changeNumberOfGuests(1L, orderTable);

		// then
		verify(savedOrderTable).setNumberOfGuests(orderTable.getNumberOfGuests());
		verify(orderTableDao).save(savedOrderTable);
	}
}