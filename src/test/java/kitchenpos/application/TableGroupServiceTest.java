package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@Mock
	private TableGroupDao tableGroupDao;

	@InjectMocks
	private TableGroupService tableGroupService;

	private TableGroup tableGroup;
	private OrderTable orderTable;

	@BeforeEach
	void setUp() {
		orderTable = new OrderTable(1L, null, 2, true);
		OrderTable orderTable2 = new OrderTable(2L, null, 3, true);

		tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));
	}

	@Test
	void createTableGroupTest() {
		when(orderTableDao.findAllByIdIn(Lists.list(1L, 2L))).thenReturn(tableGroup.getOrderTables());
		when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);
		when(orderTableDao.save(orderTable)).thenReturn(orderTable);

		assertThat(tableGroupService.create(tableGroup)).isNotNull();
	}

	@Test
	@DisplayName("테이블 그룹 생성 시 주문 테이블이 비었거나 개수가 2보다 작으면 익셉션 발생")
	void createTableGroupFailTest() {
		tableGroup = new TableGroup(1L, LocalDateTime.now(), null);
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);

		tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(new OrderTable()));
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("테이블 그룹 생성 시 저장되어있는 주문테이블과 요청 된 주문테이블의 수가 맞지 않으면 익셉션 발생")
	void createTableGroupFailTest2() {
		when(orderTableDao.findAllByIdIn(Lists.list(1L, 2L))).thenReturn(Lists.list(orderTable));

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("테이블 그룹 생성 시 저장되어있던 주문테이블이 빈 테이블이거나 이미 테이블 그룹이 존재하면 익셉션 발생")
	void createTableGroupFailTest3() {
		OrderTable orderTable = new OrderTable(1L, null, 2, false);
		when(orderTableDao.findAllByIdIn(Lists.list(1L, 2L))).thenReturn(Lists.list(orderTable));
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);

		OrderTable orderTable2 = new OrderTable(2L, 1L, 3, true);
		when(orderTableDao.findAllByIdIn(Lists.list(1L, 2L))).thenReturn(Lists.list(orderTable2));
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void ungroupTest() {
		when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(tableGroup.getOrderTables());
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.list(1L, 2L), Lists.list(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		tableGroupService.ungroup(1L);

		assertThat(tableGroup.getOrderTables().get(0).getTableGroupId()).isNull();
	}

	@Test
	@DisplayName("테이블 그룹을 해제 시 이미 완료되지 않은 주문 테이블이 존재하면 익셉션 발생")
	void ungroupFailTest() {
		when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(tableGroup.getOrderTables());
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.list(1L, 2L), Lists.list(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		tableGroupService.ungroup(1L);

		assertThat(tableGroup.getOrderTables().get(0).getTableGroupId()).isNull();
	}
}
