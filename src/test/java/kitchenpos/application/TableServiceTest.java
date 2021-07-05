package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@InjectMocks
	private TableService tableSevrice;

	@DisplayName("주문 테이블 생성을 확인")
	@Test
	void testCreateTable() {
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTableDao.save(eq(orderTable))).thenReturn(orderTable);

		tableSevrice.create(orderTable);
		verify(orderTable, times(1)).setTableGroupId(eq(null));
	}

	@DisplayName("주문 테이블 목록 반환을 확인")
	@Test
	void testTableList() {
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(1L, 1L, 3, false));
		orderTables.add(new OrderTable(2L, 1L, 3, false));

		when(orderTableDao.findAll()).thenReturn(orderTables);
		List<OrderTable> actual = tableSevrice.list();

		List<Long> actualOrderTableIds = actual.stream().map(OrderTable::getId).collect(Collectors.toList());
		List<Long> expectedOrderTableIds = orderTables.stream().map(OrderTable::getId).collect(
			Collectors.toList());
		assertThat(actualOrderTableIds).containsExactlyElementsOf(expectedOrderTableIds);
	}

	@DisplayName("주문 테이블을 비어있는 상태 변경 테스트")
	@Test
	void testChangeEmpty() {
		OrderTable orderTable = new OrderTable(1L, null, 3, true);
		OrderTable savedOrderTable = new OrderTable(1L, null, 3, false);
		Long orderTableId = 1L;

		when(orderTableDao.findById(eq(orderTableId))).thenReturn(Optional.of(savedOrderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(Mockito.eq(orderTableId), Mockito.anyList())).thenReturn(
			false);
		when(orderTableDao.save(Mockito.any())).thenReturn(savedOrderTable);

		OrderTable actual = tableSevrice.changeEmpty(orderTableId, orderTable);

		Assertions.assertThat(actual.isEmpty()).isTrue();
	}

	@DisplayName("주문 테이블이 없는경우 오류 발생")
	@Test
	void testChangeEmptyErrorNotFoundOrderTable() {
		OrderTable orderTable = new OrderTable(1L, null, 3, true);
		Long orderTableId = 1L;
		when(orderTableDao.findById(eq(orderTableId))).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeEmpty(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 주문 테이블을 찾을 수 없습니다.");
	}

	@DisplayName("주문 테이블이 단체 지정 되어있는 경우 오류 발생")
	@Test
	void testAlreadyTableGroup() {
		OrderTable orderTable = new OrderTable(1L, null, 3, true);
		Long orderTableId = 1L;
		OrderTable savedOrderTable = new OrderTable(1L, 1L, 3, false);

		when(orderTableDao.findById(eq(orderTableId))).thenReturn(Optional.of(savedOrderTable));
		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeEmpty(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("단체 지정되어있는 테이블은 변경할 수 없습니다.");
	}

	@DisplayName("주문 테이블의 상태가 COOKING, MEAL 인경우 오류 발생")
	@Test
	void testOrderTableStatusNotCompletion() {
		OrderTable orderTable = new OrderTable(1L, null, 3, true);
		Long orderTableId = 1L;
		OrderTable savedOrderTable = new OrderTable(1L, null, 3, false);

		when(orderTableDao.findById(eq(orderTableId))).thenReturn(Optional.of(savedOrderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(Mockito.eq(1L), Mockito.anyList())).thenReturn(true);
		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeEmpty(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 테이블의 주문상태가 완료되지 않아 변경할 수 없습니다");
	}

	@DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
	@Test
	void testChangeNumberOfGuests() {
		OrderTable orderTable = new OrderTable(1L, null, 2, true);
		OrderTable savedOrderTable = new OrderTable(1L, 1L, 3, false);
		int numberOfGuests = 2;

		when(orderTableDao.findById(Mockito.eq(1L))).thenReturn(Optional.of(savedOrderTable));
		when(orderTableDao.save(Mockito.any())).thenReturn(savedOrderTable);

		OrderTable actual = tableSevrice.changeNumberOfGuests(1L, orderTable);
		Assertions.assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("변경할 방문 손님 수가 0보다 작으면 오류 발생")
	@Test
	void testNumberOfGuestsUnderZero() {
		OrderTable orderTable = new OrderTable(1L, null, -1, true);
		Long orderTableId = 1L;

		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeNumberOfGuests(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("방문 손님 수는 0보다 작을 수 없습니다.");
	}

	@DisplayName("변경할 주문 테이블이 없는 경우 오류 발생")
	@Test
	void testNotFoundChangeTargetTable() {
		OrderTable orderTable = new OrderTable(1L, null, 2, true);
		Long orderTableId = 1L;
		when(orderTableDao.findById(Mockito.eq(orderTableId))).thenReturn(Optional.empty());
		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeNumberOfGuests(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 주문 테이블을 찾을 수 없습니다.");
	}

	@DisplayName("변경할 주문 테이블이 비어있는 경우")
	@Test
	void testChangeTargetOrderTableIsEmpty() {
		OrderTable orderTable = new OrderTable(1L, null, 2, true);
		OrderTable savedOrderTable = new OrderTable(1L, 1L, 3, true);
		Long orderTableId = 1L;

		when(orderTableDao.findById(Mockito.eq(orderTableId))).thenReturn(Optional.of(savedOrderTable));

		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeNumberOfGuests(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블입니다.");
	}
}