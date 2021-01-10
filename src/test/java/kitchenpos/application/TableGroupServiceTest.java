package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TestDomainConstructor;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@Mock
	private TableGroupDao tableGroupDao;
	@InjectMocks
	private TableGroupService tableGroupService;

	@Test
	@DisplayName("단체를 지정할 수 있다.")
	void create() {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, true, 1L);
		OrderTable orderTable2 = TestDomainConstructor.orderTableWithId(null, 0, true, 2L);
		List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);

		Long tableGroupId = 1L;
		TableGroup tableGroup = TestDomainConstructor.tableGroup(Arrays.asList(orderTable, orderTable2), null);
		TableGroup savedTableGroup = TestDomainConstructor.tableGroupWithId(Arrays.asList(orderTable, orderTable2),
			LocalDateTime.now(), tableGroupId);
		when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
		when(tableGroupDao.save(tableGroup)).thenReturn(savedTableGroup);
		when(orderTableDao.save(any())).thenReturn(orderTable, orderTable2);

		//when
		TableGroup result = tableGroupService.create(tableGroup);

		//then
		assertThat(result.getId()).isEqualTo(tableGroupId);
		assertThat(result.getOrderTables().stream()
			.filter(table -> tableGroupId.equals(table.getTableGroupId()))
			.count()).isEqualTo(2);
		assertThat(result.getOrderTables().stream()
			.filter(OrderTable::isEmpty)
			.count()).isEqualTo(0);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 없으면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForNullTable() {
		//given
		TableGroup tableGroup = TestDomainConstructor.tableGroup(null, null);

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 1개이면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForOneTable() {
		//given
		TableGroup tableGroup = TestDomainConstructor.tableGroup(Arrays.asList(mock(OrderTable.class)), null);

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForNotExistTable() {
		//given
		TableGroup tableGroup = TestDomainConstructor.tableGroup(Arrays.asList(mock(OrderTable.class), mock(OrderTable.class)), null);
		when(orderTableDao.findAllByIdIn(any())).thenReturn(new ArrayList<>());

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 빈 테이블이 아니면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForNotEmptyTable() {
		//given
		OrderTable notEmptyTable = TestDomainConstructor.orderTableWithId(null, 0, false, 1L);
		TableGroup tableGroup = TestDomainConstructor.tableGroup(Arrays.asList(mock(OrderTable.class), mock(OrderTable.class)), null);
		when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(notEmptyTable, mock(OrderTable.class)));

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 이미 그룹에 포함된 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForAlreadyHaveGroup() {
		//given
		OrderTable havingGroupTable = TestDomainConstructor.orderTableWithId(3L, 0, true, 1L);
		TableGroup tableGroup = TestDomainConstructor.tableGroup(Arrays.asList(mock(OrderTable.class), mock(OrderTable.class)), null);
		when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(havingGroupTable, mock(OrderTable.class)));

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정을 해지할 수 있다.")
	void ungroup() {
		//given
		Long tableGroupId = 1L;
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(tableGroupId, 0, false, 1L);
		OrderTable orderTable2 = TestDomainConstructor.orderTableWithId(tableGroupId, 0, false, 2L);
		List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);

		when(orderTableDao.findAllByTableGroupId(any())).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

		//when
		tableGroupService.ungroup(tableGroupId);

		//then
		assertThat(orderTables.stream()
			.filter(table -> table.getTableGroupId() == null)
			.collect(Collectors.toList())).containsExactlyInAnyOrder(orderTable, orderTable2);
	}

	@Test
	@DisplayName("단체 지정을 해지 시, 주문 상태가 조리 또는 식사인 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void ungroupCookingTable() {
		when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(mock(OrderTable.class), mock(OrderTable.class)));
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

		//when-then
		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
