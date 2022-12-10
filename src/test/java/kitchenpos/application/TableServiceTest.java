package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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

	@Mock OrderDao orderDao;
	@Mock OrderTableDao orderTableDao;

	TableService tableService;

	@BeforeEach
	void setup() {
		tableService = new TableService(orderDao, orderTableDao);
	}

	@Test
	void testCreateTable() {
		OrderTable orderTable = createOrderTable();
		when(orderTableDao.save(orderTable)).thenAnswer(returnsFirstArg());

		OrderTable createdOrderTable = tableService.create(orderTable);

		verify(orderTableDao, times(1)).save(orderTable);
		assertThat(createdOrderTable).isEqualTo(orderTable);
	}

	@Test
	void testListOrderTable() {
		List<OrderTable> orderTables = createOrderTables();
		when(orderTableDao.findAll()).thenReturn(orderTables);

		List<OrderTable> savedOrderTables = tableService.list();

		verify(orderTableDao, times(1)).findAll();
		assertThat(savedOrderTables).containsExactlyElementsOf(orderTables);
	}

	@Test
	void changeEmpty() {
		OrderTable orderTable = createOrderTable();
		orderTable.setEmpty(true);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(
			orderTable.getId(),
			Lists.newArrayList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
			.thenReturn(false);
		when(orderTableDao.save(any())).thenAnswer(returnsFirstArg());

		OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

		verify(orderTableDao, times(1)).save(orderTable);
		assertThat(changedOrderTable.isEmpty()).isTrue();
	}

	@Test
	void testChangeNumberOfGuests() {
		OrderTable orderTable = createOrderTable();
		int expectedNumberOfGuests = orderTable.getNumberOfGuests() + 1;
		orderTable.setNumberOfGuests(expectedNumberOfGuests);

		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderTableDao.save(any())).thenAnswer(returnsFirstArg());

		OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

		assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
		verify(orderTableDao, times(1)).save(orderTable);
	}

	@Test
	void testChangeNumberOfGuestsBelowThanZero() {
		OrderTable orderTable = createOrderTable();
		int invalidNumberOfGuests = -1;
		orderTable.setNumberOfGuests(invalidNumberOfGuests);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testChangeNumberOfGuestsWithNotExistsTableId() {
		OrderTable orderTable = createOrderTable();
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testChangeEmptyWhenOrderStatusNotCompletion() {
		OrderTable orderTable = createOrderTable();
		orderTable.setEmpty(true);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(true);

		assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	private List<OrderTable> createOrderTables() {
		return Lists.newArrayList(
			createOrderTable(),
			createOrderTable(),
			createOrderTable()
		);
	}

	private OrderTable createOrderTable() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(10);
		return orderTable;
	}
}
