package kitchenpos.application;

import kitchenpos.MockitoTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TableServiceTest extends MockitoTest {

	@InjectMocks
	private TableService tableService;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	private OrderTable savedOrderTable;

	@BeforeEach
	void setUp() {
		savedOrderTable = MockFixture.orderTable(1L, null, false, 0);

		given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
	}

	@DisplayName("새로운 테이블을 생선한다.")
	@Test
	void create() {
		// given
		OrderTable orderTable = MockFixture.orderTableForCreate();

		// when
		tableService.create(orderTable);

		// then
		Mockito.verify(orderTable, times(1)).setTableGroupId(isNull());
		Mockito.verify(orderTableDao, times(1)).save(orderTable);
	}

	@DisplayName("테이블 리스트를 반환한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 999})
	void list(int size) {
		// given
		List<OrderTable> orderTables = MockFixture.anyOrderTables(size);
		given(orderTableDao.findAll()).willReturn(orderTables);

		// when then
		assertThat(tableService.list()).hasSize(size);
	}

	@DisplayName("테이블의 비어있는 상태를 바꾼다.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmpty(boolean isEmpty) {
		// given
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);

		// when
		tableService.changeEmpty(savedOrderTable.getId(), MockFixture.orderTable(null, null, isEmpty, 0));

		// then
		Mockito.verify(savedOrderTable).setEmpty(isEmpty);
		Mockito.verify(orderTableDao).save(savedOrderTable);
	}

	@DisplayName("이미 단체지정된 테이블의 비어있는 상태 변경시 예외 발생.")
	@Test
	void changeEmpty_AlreadyTableGroupIncluded() {
		// given
		given(savedOrderTable.getTableGroupId()).willReturn(5L);

		// when then
		assertThatThrownBy(() -> tableService.changeEmpty(
				savedOrderTable.getId(), MockFixture.orderTable(null, null, true, 0)))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("특정 상태인 테이블을 비우려고 하면 예외 발생.")
	@Test
	void changeEmpty_StatusWrong() {
		// given
		final boolean hasWrongStatus = true;
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(hasWrongStatus);

		// when
		assertThatThrownBy(() -> tableService.changeEmpty(
				savedOrderTable.getId(), MockFixture.orderTable(null, null, true, 0)))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블의 인원수를 변경한다.")
	@Test
	void changeNumberOfGuests() {
		// given
		given(orderTableDao.save(savedOrderTable)).willReturn(savedOrderTable);
		final int numberOfGuests = 20;

		// when
		OrderTable param = MockFixture.orderTable(null, null, false, numberOfGuests);
		savedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), param);

		// then
		verify(orderTableDao).save(savedOrderTable);
		verify(savedOrderTable).setNumberOfGuests(numberOfGuests);
	}

	@DisplayName("테이블 인원 변경시 음수로 설정하면 예외 발생.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -999})
	void changeNumberOfGuests_GuestWrong(int numberOfGuests) {
		// when & then
		OrderTable param = MockFixture.orderTable(null, null, false, numberOfGuests);
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), param))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 인원 변경시 테이블이 비어있는 경우 예외 발생.")
	@Test
	void changeNumberOfGuests_TableEmpty() {
		// given
		given(savedOrderTable.isEmpty()).willReturn(true);

		// when & then
		OrderTable param = MockFixture.orderTable(null, null, false, 20);
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), param))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
