package kitchenpos.application.mock;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@InjectMocks
	private TableGroupService tableGroupService;
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@Mock
	private TableGroupDao tableGroupDao;

	@DisplayName("테이블 리스트가 2보다 작으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException1() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(createTableGroup()));
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(createTableGroup(1L)));
	}

	@DisplayName("등록되지 않은 테이블 지정시 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException2() {
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(createOrderTables(1L));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(createTableGroup(1L, 9L)));
	}

	@DisplayName("주문 테이블 지정시 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException3() {
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(
			createOrderTable(1L, 0, true, null),
			createOrderTable(2L, 0, false, null)
		));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(createTableGroup(1L, 2L)));
	}

	@DisplayName("단체 테이블 지정시 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException4() {
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(
			createOrderTable(1L, 0, true, null),
			createOrderTable(2L, 0, false, 1L)
		));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(createTableGroup(1L, 2L)));
	}

	@DisplayName("단체 지정 등록")
	@Test
	void create() {
		List<OrderTable> orderTables = Arrays.asList(
			createOrderTable(1L, 0, true, null),
			createOrderTable(2L, 0, true, null)
		);
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);
		when(tableGroupDao.save(any(TableGroup.class))).thenAnswer(invocation -> {
			TableGroup tableGroup = invocation.getArgument(0, TableGroup.class);
			tableGroup.setId(1L);
			return tableGroup;
		});

		TableGroup resultGroup = tableGroupService.create(createTableGroup(1L, 2L));

		verify(orderTableDao, times(2)).save(orderTableDao.save((any(OrderTable.class))));
		assertThat(resultGroup.getId()).isNotNull();
		List<OrderTable> resultOrderTables = resultGroup.getOrderTables();
		assertThat(resultOrderTables).hasSize(2);
		resultOrderTables.forEach(
			orderTable -> assertThat(orderTable.getTableGroupId()).isEqualTo(resultGroup.getId()));
	}

	@DisplayName("주문 상태가 요리 중 또는 식사 중이면 IllegalArgumentException 발생")
	@Test
	void ungroup_ThrowIllegalArgumentException() {
		List<OrderTable> orderTables = Arrays.asList(
			createOrderTable(1L, 2, false, 1L),
			createOrderTable(2L, 2, false, 1L)
		);
		when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.ungroup(1L));
	}

	@DisplayName("단체 지정 해제")
	@Test
	void ungroup() {
		List<OrderTable> orderTables = Arrays.asList(
			createOrderTable(1L, 2, false, 1L),
			createOrderTable(2L, 2, false, 1L)
		);
		when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

		tableGroupService.ungroup(1L);

		verify(orderTableDao, times(2)).save(any(OrderTable.class));
		orderTables.forEach(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());
	}
}
