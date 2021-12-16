package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;

@DisplayName("테이블 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

	@Mock
	OrderDao orderDao;

	@Mock
	OrderTableDao orderTableDao;

	@Mock
	OrderTable orderTable;

	@InjectMocks
	private TableService tableService;

	@DisplayName("테이블을 생성하는 테스트")
	@Test
	void createTable() {
		// when
		when(orderTableDao.save(orderTable)).thenReturn(orderTable);

		// then
		assertThat(tableService.create(orderTable)).isEqualTo(orderTable);
	}

	@DisplayName("테이블 목록을 조회하는 테스트")
	@Test
	void getList() {
		// when
		when(orderTableDao.findAll()).thenReturn(Collections.singletonList(orderTable));

		// then
		assertThat(tableService.list()).containsExactly(orderTable);
	}

	@DisplayName("상태를 변경하려는 테이블이 존재하지 않을 경우 예외처리 테스트")
	@Test
	void changeEmptyUnknownOrderTable() {
		// given // when // then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(anyLong(), orderTable);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상태를 변경하려는 테이블의 주문 상태가 완료가 아닌 경우 예외처리 테스트")
	@Test
	void changeEmptyOrderStatusNotCompletion() {
		// given
		given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
		given(orderTable.getTableGroupId()).willReturn(null);

		// when
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

		// then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(orderTable.getId(), orderTable);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블의 상태를 변경하는 테스트")
	@Test
	void changeEmpty() {
		// given
		given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
		given(orderTable.getTableGroupId()).willReturn(null);
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
		given(orderTable.isEmpty()).willReturn(true);

		// when
		when(orderTableDao.save(orderTable)).thenReturn(orderTable);

		// then
		assertThat(tableService.changeEmpty(anyLong(), orderTable).isEmpty()).isTrue();
	}

	@DisplayName("변경하려는 손님 인원이 0미만일 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestUnderZeroGuest() {
		// when
		when(orderTable.getNumberOfGuests()).thenReturn(-1);

		// then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("손님 인원을 변경하려는 테이블이 존재하지 않은 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestUnknownOrderTable() {
		// given
		given(orderTable.getNumberOfGuests()).willReturn(2);

		// when
		when(orderTableDao.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		// then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("손님 인원을 변경하려는 테이블이 비어있는 상태인 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestEmptyOrderTable() {
		// given
		given(orderTable.getNumberOfGuests()).willReturn(2);
		given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

		// when
		when(orderTable.isEmpty()).thenReturn(true);

		// then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블의 손님 인원을 변경하는 테스트")
	@Test
	void changeNumberOfGuests() {
		// given
		given(orderTable.getNumberOfGuests()).willReturn(2);
		given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
		given(orderTable.isEmpty()).willReturn(false);

		// when
		when(orderTableDao.save(orderTable)).thenReturn(orderTable);

		// then
		assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable).getNumberOfGuests()).isEqualTo(2);
	}
}
