package kitchenpos.application;

import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@DisplayName("테이블")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	OrderDao orderDao;
	@Mock
	OrderTableDao orderTableDao;

	@DisplayName("테이블을 생성할 수 있다.")
	@Test
	void create(){
		// given
		OrderTable orderTable = new OrderTable();
		OrderTable savedOrderTable = mock(OrderTable.class);
		when(savedOrderTable.getId()).thenReturn(1L);
		when(orderTableDao.save(orderTable)).thenReturn(savedOrderTable);
		TableService tableService = new TableService(orderDao, orderTableDao);

		// when
		OrderTable createdOrderTable = tableService.create(orderTable);

		// then
		assertThat(createdOrderTable.getId()).isNotNull();
	}

	@DisplayName("테이블 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		OrderTable savedOrderTable = mock(OrderTable.class);
		when(orderTableDao.findAll()).thenReturn(Arrays.asList(savedOrderTable));
		TableService tableService = new TableService(orderDao, orderTableDao);

		// when
		List<OrderTable> orderTables = tableService.list();

		// then
		assertThat(orderTables).contains(savedOrderTable);
	}

	@DisplayName("테이블을 비울 수 있다.")
	@Test
	void changeEmpty(){
		// given
		Long orderTableId = 1L;
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(orderTableId);
		when(orderTable.getTableGroupId()).thenReturn(null);

		when(orderTableDao.findById(orderTable.getId())).thenReturn(of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

		OrderTable savedOrderTable = mock(OrderTable.class);
		when(savedOrderTable.isEmpty()).thenReturn(true);
		when(orderTableDao.save(orderTable)).thenReturn(savedOrderTable);
		TableService tableService = new TableService(orderDao, orderTableDao);

		// when
		OrderTable finalSavedOrderTable = tableService.changeEmpty(orderTableId, orderTable);

		// then
		assertThat(finalSavedOrderTable.isEmpty()).isTrue();
	}

	@DisplayName("존재하지 않는 테이블은 비울 수 없다.")
	@Test
	void notExistOrderTableCannotChangeEmpty(){
		// given
		Long orderTableId = 1L;
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(orderTableId);

		when(orderTableDao.findById(orderTable.getId())).thenReturn(of(orderTable));
		TableService tableService = new TableService(orderDao, orderTableDao);

		// when - then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요리중이거나 식사중인 테이블이 있는 경우 테이블을 비울 수 없다.")
	@Test
	void mealOrCookingOrderTableCannotNotChangeEmpty(){
		// given
		Long orderTableId = 1L;
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(orderTableId);
		when(orderTable.getTableGroupId()).thenReturn(null);

		when(orderTableDao.findById(orderTable.getId())).thenReturn(of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
		TableService tableService = new TableService(orderDao, orderTableDao);

		// when - then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(orderTableId, orderTable);
		}).isInstanceOf(IllegalArgumentException.class);
	}

}