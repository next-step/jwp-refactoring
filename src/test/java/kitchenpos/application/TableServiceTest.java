package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import kitchenpos.domain.TestDomainConstructor;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@InjectMocks
	private TableService tableService;

	@Test
	@DisplayName("메뉴를 등록할 수 있다.")
	void create() {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTable(null, 0, true);
		OrderTable savedOrderTable = TestDomainConstructor.orderTableWithId(null, 0, true, 1L);
		when(orderTableDao.save(orderTable)).thenReturn(savedOrderTable);

		//when
		OrderTable result = tableService.create(orderTable);

		//then
		assertThat(result).isEqualTo(savedOrderTable);
	}

	@Test
	@DisplayName("빈 테이블로 설정 또는 해지할 수 있다.")
	void changeEmpty() {
		//given
		Long orderTableId = 1L;
		boolean empty = true;
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, empty, orderTableId);
		OrderTable savedOrderTable = TestDomainConstructor.orderTableWithId(null, 0, !empty, orderTableId);
		when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
		when(orderTableDao.save(any())).thenReturn(savedOrderTable);

		//when
		OrderTable result = tableService.changeEmpty(orderTableId, orderTable);

		//then
		assertThat(result.isEmpty()).isEqualTo(empty);
	}

	@Test
	@DisplayName("빈 테이블 설정 변경 시, 테이블이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void changeEmptyNotExistOrderTable() {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, true, 1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		//when-then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("빈 테이블 설정 변경 시, 단체 지정된 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void changeEmptyGroupTable() {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(1L, 0, true, 1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		//when-then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("빈 테이블 설정 변경 시, 주문 상태가 조리 또는 식사인 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void changeEmptyCookingTable() {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, true, 1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

		//when-then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("방문한 손님 수를 입력할 수 있다.")
	void changeNumberOfGuests() {
		//given
		Long orderTableId = 1L;
		int numberOfGuests = 4;
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, numberOfGuests, false, orderTableId);
		OrderTable savedOrderTable = TestDomainConstructor.orderTableWithId(null, 0, false, orderTableId);
		when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
		when(orderTableDao.save(any())).thenReturn(savedOrderTable);

		//when
		OrderTable result = tableService.changeNumberOfGuests(orderTableId, orderTable);

		//then
		assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@Test
	@DisplayName("방문한 손님 수를 입력 시, 손님 수를 0 명 미만으로 입력하면 IllegalArgumentException을 throw 해야한다.")
	void changeNegativeNumberOfGuests() {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(1L, -2, false, 1L);

		//when-then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("방문한 손님 수를 입력 시, 빈 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void changeNumberOfGuestsEmptyTable() {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, true, 1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		//when-then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
