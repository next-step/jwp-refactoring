package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Test
	@DisplayName("테이블 생성 테스트")
	public void createTableSuccessTest() {
		//given
		OrderTable orderTable = new OrderTable(null, 1L, 0, true);
		when(orderTableDao.save(orderTable)).thenReturn(new OrderTable(1L, null, 0, true));
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		OrderTable save = tableService.create(orderTable);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(1L);
		assertThat(save.getTableGroupId()).isNull();
	}

	@Test
	@DisplayName("테이블 빈 상태로 변경 테스트")
	public void changeEmptyTableTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 0, true);
		OrderTable findOrderTable = new OrderTable(1L, null, 0, false);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(findOrderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
			.thenReturn(false);
		OrderTable changedOrderTable = new OrderTable(1L, null, 0, true);
		when(orderTableDao.save(findOrderTable)).thenReturn(changedOrderTable);
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		OrderTable save = tableService.changeEmpty(1L, changeParamOrderTable);

		//then
		assertThat(save).isNotNull();
		assertThat(save.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("주문테이블 없어서 빈 상태로 변경 실패")
	public void changeEmptyTableFailNotExistedOrderTableTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 0, false);
		when(orderTableDao.findById(1L)).thenReturn(Optional.empty());
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		//then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, changeParamOrderTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문테이블이 존재하지 않습니다");
	}

	@Test
	@DisplayName("테이블그룹Id를 가지고 있어서 주문테이블 빈 상태로 변경 실패")
	public void changeEmptyTableFailExistedTableGroupIdTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 0, false);
		OrderTable findOrderTable = new OrderTable(1L, 1L, 0, true);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(findOrderTable));
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		assertThatThrownBy(() -> tableService.changeEmpty(1L, changeParamOrderTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("빈 테이블은 그룹ID를 가질 수 없습니다");
	}

	@Test
	@DisplayName("테이블이 계산완료되지 않아서 빈 상태로 변경 실패")
	public void changeEmptyTableFailNotCompleteTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 0, false);
		OrderTable findOrderTable = new OrderTable(1L, null, 0, true);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(findOrderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
			.thenReturn(true);
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		assertThatThrownBy(() -> tableService.changeEmpty(1L, changeParamOrderTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("테이블이 계산완료 되지 않았습니다");
	}

	@Test
	@DisplayName("테이블 인원수 변경 테스트")
	public void changeNumberOfGuestsTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 3, false);
		OrderTable findOrderTable = new OrderTable(1L, 1L, 0, false);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(findOrderTable));
		OrderTable changedOrderTable = new OrderTable(1L, 1L, 3, false);
		when(orderTableDao.save(findOrderTable)).thenReturn(changedOrderTable);
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		OrderTable save = tableService.changeNumberOfGuests(1L, changeParamOrderTable);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getNumberOfGuests()).isEqualTo(3);
	}

	@Test
	@DisplayName("테이블 인원수가 0보다 작아서 변경 실패")
	public void changeNumberOfGuestsLessThanZeroFailTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, -1, false);
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeParamOrderTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("인원수는 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("테이블이 비어있어서 인원수 변경 실패")
	public void changeNumberOfGuestsFailTableIsEmptyTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 3, false);
		OrderTable findOrderTable = new OrderTable(1L, 1L, 0, true);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(findOrderTable));
		TableService tableService = new TableService(orderDao, orderTableDao);

		//when
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeParamOrderTable))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("비어 있는 테이블입니다");
	}

}
