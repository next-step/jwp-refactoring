package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableRequest;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	private OrderTableRequest orderTableWithFivePeople;
	private OrderTableRequest orderTableWithTenPeople;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private TableService tableService;

	@BeforeEach
	void setUp() {
		orderTableWithFivePeople = new OrderTableRequest(1L, null, 5, false);
		orderTableWithTenPeople = new OrderTableRequest(2L, null, 10, false);
	}

	@DisplayName("테이블을 등록한다.")
	@Test
	void createTestInHappyCase() {
		// Given
		when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeople);
		// When
		OrderTableRequest orderTable = tableService.create(orderTableWithFivePeople);
		// Then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
	}

	@DisplayName("테이블을 조회한다.")
	@Test
	void listInHappyCase() {
		// Given
		when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		// When
		List<OrderTableRequest> orderTables = tableService.list();
		// Then
		assertThat(orderTables.size()).isEqualTo(2);
	}

	@DisplayName("테이블을 비우거나 채운다.")
	@Test
	void changeEmptyInHappyCase() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTableWithFivePeople));
		lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		lenient().when(orderTableDao.save(any())).thenReturn(new OrderTableRequest(1L, null, 5, true));
		// When
		OrderTableRequest orderTable = tableService.changeEmpty(1L, orderTableWithFivePeople);
		// Then
		assertThat(orderTable.isEmpty()).isEqualTo(true);
	}

	@DisplayName("요청한 주문 테이블은 기 존재해야 한다.")
	@Test
	void changeEmptyWithNotExistsOrderTable() {
		// Given
		lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		lenient().when(orderTableDao.save(any())).thenReturn(new OrderTableRequest(1L, null, 5, true));
		// When, Then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableWithFivePeople)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요청한 주문 테이블은 테이블 그룹 정보를 가질 수 없다.")
	@Test
	void changeEmptyWithOrderTableHavingTableGroup() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTableRequest(1L, 1L, 5, false)));
		lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		lenient().when(orderTableDao.save(any())).thenReturn(new OrderTableRequest(1L, null, 5, true));
		// When, Then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableWithFivePeople)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("COOKING이거나 MEAL인 테이블은 비울 수 없다.")
	@Test
	void changeEmptyWithCookingOrMealTable() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTableRequest(1L, 1L, 5, false)));
		lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
		lenient().when(orderTableDao.save(any())).thenReturn(new OrderTableRequest(1L, null, 5, true));
		// When, Then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableWithFivePeople)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블의 인원수를 변경한다.")
	@Test
	void changeNumberOfGuestsInHappyCase() {
		// Given
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTableWithFivePeople));
		when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeople);
		// When
		OrderTableRequest orderTable = tableService.changeNumberOfGuests(1L, orderTableWithTenPeople);
		// Then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
	}

	@DisplayName("변경 테이블 인원 수는 0 이상이다.")
	@Test
	void changeNumberOfGuestsWithMinusNumber() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTableWithFivePeople));
		lenient().when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeople);
		// When, Then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(2L, null, -10, false))).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요청한 테이블은 기 존재해야 한다.")
	@Test
	void changeNumberOfGuestsWithNotExistsOrderTable() {
		// Given
		lenient().when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeople);
		// When, Then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(2L, null, -10, false))).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("비어 있는 테이블은 인원 변경이 불가하다.")
	@Test
	void changeNumberOfGuestsWithEmptyOrderTable() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTableRequest(1L, null, 5, true)));
		lenient().when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeople);
		// When, Then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(2L, null, -10, false))).isInstanceOf(IllegalArgumentException.class);
	}
}
