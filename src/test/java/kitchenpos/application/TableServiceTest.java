package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private TableService tableService;

	@Test
	void createTestInHappyCase() {
		// Given
		when(orderTableDao.save(any())).thenReturn(new OrderTable());
		// When
		OrderTable orderTable = tableService.create(new OrderTable());
		// Then
		assertThat(orderTable).isEqualTo(new OrderTable());
	}

	@Test
	void listInHappyCase() {
		// Given
		when(orderTableDao.findAll()).thenReturn(Arrays.asList(new OrderTable()));
		// When
		List<OrderTable> orderTables = tableService.list();
		// Then
		assertThat(orderTables.size()).isEqualTo(1);
	}

	@Test
	void changeEmptyInHappyCase() {
		// Given
		when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable()));
		lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
		when(orderTableDao.save(any())).thenReturn(new OrderTable());
		// When
		OrderTable orderTable = tableService.changeEmpty(1L, new OrderTable());
		// Then
		assertThat(orderTable).isEqualTo(new OrderTable());
	}

	@Test
	void changeNumberOfGuestsInHappyCase() {
		// Given
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTable(1L, 5, false)));
		when(orderTableDao.save(any())).thenReturn(new OrderTable(1L, 10, false));
		// When
		OrderTable orderTable = tableService.changeNumberOfGuests(1L, new OrderTable(1L, 10, false));
		// Then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
	}
}
