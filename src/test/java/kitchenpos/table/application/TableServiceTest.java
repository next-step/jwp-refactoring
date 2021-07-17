package kitchenpos.table.application;

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
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	private OrderTableRequest orderTableWithFivePeopleRequest;
	private OrderTableRequest orderTableWithTenPeopleRequest;
	private OrderTable orderTableWithFivePeople;
	private OrderTable orderTableWithTenPeople;

	@Mock
	private OrderRepository orderDao;

	@Mock
	private OrderTableRepository orderTableDao;

	@InjectMocks
	private TableService tableService;

	@BeforeEach
	void setUp() {
		orderTableWithFivePeopleRequest = new OrderTableRequest(1L, null, 5, false);
		orderTableWithTenPeopleRequest = new OrderTableRequest(2L, null, 10, false);
		orderTableWithFivePeople = new OrderTable(new NumberOfGuests(5), false);
		orderTableWithTenPeople = new OrderTable(new NumberOfGuests(10), false);
	}

	@DisplayName("테이블을 등록한다.")
	@Test
	void createTestInHappyCase() {
		// Given
		when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeopleRequest);
		// When
		OrderTableResponse orderTable = tableService.create(orderTableWithFivePeopleRequest);
		// Then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(5));
	}

	@DisplayName("테이블을 조회한다.")
	@Test
	void listInHappyCase() {
		// Given
		when(orderTableDao.findAll()).thenReturn(Arrays.asList(new OrderTable(new NumberOfGuests(5), false), new OrderTable(new NumberOfGuests(10), false)));
		// When
		List<OrderTableResponse> orderTables = tableService.list();
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
		OrderTableResponse orderTable = tableService.changeEmpty(1L, orderTableWithFivePeopleRequest);
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
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableWithFivePeopleRequest)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요청한 주문 테이블은 테이블 그룹 정보를 가질 수 없다.")
	@Test
	void changeEmptyWithOrderTableHavingTableGroup() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTableWithFivePeople));
		lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		lenient().when(orderTableDao.save(any())).thenReturn(new OrderTableRequest(1L, null, 5, true));
		// When, Then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableWithFivePeopleRequest)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("COOKING이거나 MEAL인 테이블은 비울 수 없다.")
	@Test
	void changeEmptyWithCookingOrMealTable() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTableWithFivePeople));
		lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
		lenient().when(orderTableDao.save(any())).thenReturn(new OrderTableRequest(1L, null, 5, true));
		// When, Then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableWithFivePeopleRequest)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블의 인원수를 변경한다.")
	@Test
	void changeNumberOfGuestsInHappyCase() {
		// Given
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTableWithFivePeople));
		when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeopleRequest);
		// When
		OrderTableResponse orderTable = tableService.changeNumberOfGuests(1L, orderTableWithTenPeopleRequest);
		// Then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
	}

	@DisplayName("변경 테이블 인원 수는 0 이상이다.")
	@Test
	void changeNumberOfGuestsWithMinusNumber() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTableWithFivePeople));
		lenient().when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeopleRequest);
		// When, Then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(2L, null, -10, false))).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요청한 테이블은 기 존재해야 한다.")
	@Test
	void changeNumberOfGuestsWithNotExistsOrderTable() {
		// Given
		lenient().when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeopleRequest);
		// When, Then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(2L, null, -10, false))).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("비어 있는 테이블은 인원 변경이 불가하다.")
	@Test
	void changeNumberOfGuestsWithEmptyOrderTable() {
		// Given
		lenient().when(orderTableDao.findById(1L)).thenReturn(Optional.of(new OrderTable(new NumberOfGuests(5), true)));
		lenient().when(orderTableDao.save(any())).thenReturn(orderTableWithFivePeopleRequest);
		// When, Then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(2L, null, -10, false))).isInstanceOf(IllegalArgumentException.class);
	}
}
