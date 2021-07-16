package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
	private OrderTable orderTableWithFivePeople;
	private OrderTable orderTableWithTenPeople;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	@InjectMocks
	private TableGroupService tableGroupService;

	@BeforeEach
	void setUp() {
		orderTableWithFivePeople = new OrderTable(1L, null, 5, true);
		orderTableWithTenPeople = new OrderTable(2L, null, 10, true);
	}

	@DisplayName("단체 지정을 등록한다.")
	@Test
	void createTestInHappyCase() {
		// given
		when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		when(tableGroupDao.save(any())).thenReturn(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		// when
		TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		// then
		assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
	}

	@DisplayName("단체 지정을 할 때에는 요청 주문 테이블이 2보다 커야 한다.")
	@Test
	void createTestWithOneOrderTable() {
		// given
		lenient().when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		lenient().when(tableGroupDao.save(any())).thenReturn(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople))));
	}

	@DisplayName("주문 테이블은 먼저 등록되어 있어야 한다.")
	@Test
	void createTestWithNotExistsOrderTable() {
		// given
		lenient().when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(null);
		lenient().when(tableGroupDao.save(any())).thenReturn(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople))));
	}

	@DisplayName("기 단체 지정된 주문 테이블은 새롭게 단체 지정할 수 없다.")
	@Test
	void createTestWithAlreadyTableGroupedOrderTable() {
		// given
		lenient().when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(new OrderTable(1L, 1L, 5, true), new OrderTable(2L, null, 10, true)));
		lenient().when(tableGroupDao.save(any())).thenReturn(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople))));
	}

	@DisplayName("요청 주문 테이블은 비어 있어야만 한다.")
	@Test
	void createTestWithEmptyOrderTable() {
		// given
		lenient().when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(new OrderTable(1L, null, 5, false), new OrderTable(2L, null, 10, true)));
		lenient().when(tableGroupDao.save(any())).thenReturn(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople))));
	}

	@DisplayName("단체 지정 해제한다.")
	@Test
	void ungroupTestInHappyCase() {
		// given
		when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		// when
		tableGroupService.ungroup(1L);
		// then
	}

	@DisplayName("COOKING, MEAL 상태인 주문 테이블이 존재할 경우 단체 지정을 해제할 수 없다.")
	@Test
	void ungroupTestWithCookingOrMealStatus() {
		// given
		when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
		// when, then
		assertThatThrownBy(() -> tableGroupService.ungroup(1L));
	}
}
