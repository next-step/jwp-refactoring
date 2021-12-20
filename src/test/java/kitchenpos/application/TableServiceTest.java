package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private OrderDao orderDao;

	@InjectMocks
	private TableService tableService;

	@DisplayName("테이블을 등록할 수 있다")
	@Test
	void create() {
		//given
		OrderTable orderTable = new OrderTable(0, true);
		given(orderTableDao.save(orderTable))
			.willReturn(orderTable);

		//when
		OrderTable savedOrderTable = tableService.create(orderTable);

		//then
		Assertions.assertThat(orderTable).isEqualTo(savedOrderTable);
	}

	@DisplayName("테이블 목록을 조회할 수 있다.")
	@Test
	void list() {
		//given
		List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L,0, true),
			new OrderTable(2L,6, false));
		given(tableService.list())
			.willReturn(orderTables);

		//when
		List<OrderTable> findOrderTables = tableService.list();

		//then
		assertThat(findOrderTables.size()).isEqualTo(orderTables.size());
		assertThat(findOrderTables).containsAll(orderTables);
	}

	@DisplayName("테이블 상태를 변경할 수 있다")
	@Test
	void changeEmpty() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, true);
		given(orderTableDao.findById(any()))
			.willReturn(Optional.of(orderTable));

		given(orderTableDao.save(orderTable))
			.willReturn(orderTable);

		//when
		OrderTable changeOrderTable = new OrderTable(false);
		OrderTable savedOrderTable = tableService.changeEmpty(orderTable.getId(), changeOrderTable);

		//then
		Assertions.assertThat(savedOrderTable.isEmpty()).isEqualTo(changeOrderTable.isEmpty());
	}

	@DisplayName("테이블 그룹에 속해있는 테이블은 상태를 변경할 수 없다.")
	@Test
	void changeEmpty_exception1() {
		//given
		OrderTable orderTable = new OrderTable(1L, 1L,0, true);
		given(orderTableDao.findById(any()))
			.willReturn(Optional.of(orderTable));

		//when, then
		OrderTable changeOrderTable = new OrderTable(false);
		assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 주문 상태가 조리, 식사인 주문이 존재하면 상태를 변경할 수 없다.")
	@Test
	void changeEmpty_exception2() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, true);
		given(orderTableDao.findById(any()))
			.willReturn(Optional.of(orderTable));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList()))
			.willReturn(true);

		//when, then
		OrderTable changeOrderTable = new OrderTable(false);
		assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 방문 손님 수를 변경할 수 있다.")
	@Test
	void changeNumberOfGuests() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, false);
		given(orderTableDao.findById(any()))
			.willReturn(Optional.of(orderTable));

		given(orderTableDao.save(orderTable))
			.willReturn(orderTable);

		//when
		OrderTable changeOrderTable = new OrderTable(6);
		OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable);

		//then
		Assertions.assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(changeOrderTable.getNumberOfGuests());
	}

	@DisplayName("변경 요청 방문 손님 수는 0 이상 이어야 한다.")
	@Test
	void changeNumberOfGuests_exception1() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, false);

		//when, then
		OrderTable changeOrderTable = new OrderTable(-1);
		Assertions.assertThatThrownBy(()->tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블은 방문 손님 수를 변경할 수 없다.")
	@Test
	void changeNumberOfGuests_exception2() {
		//given
		OrderTable orderTable = new OrderTable(1L, 0, true);
		given(orderTableDao.findById(any()))
			.willReturn(Optional.of(orderTable));

		//when, then
		OrderTable changeOrderTable = new OrderTable(6);
		Assertions.assertThatThrownBy(()->tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
