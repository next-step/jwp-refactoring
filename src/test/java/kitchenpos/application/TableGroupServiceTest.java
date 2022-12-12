package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
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

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	OrderDao orderDao;
	@Mock
	OrderTableDao orderTableDao;
	@Mock
	TableGroupDao tableGroupDao;

	@InjectMocks
	TableGroupService tableGroupService;


	@Test
	@DisplayName("테이블 그룹 생성")
	void testCreateMenuGroup() {
		// given
		List<OrderTable> orderTables = createOrderTables(true);
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);
		when(tableGroupDao.save(any())).thenAnswer(returnsFirstArg());

		// when
		TableGroup tableGroup = createTableGroup(orderTables);
		tableGroupService.create(tableGroup);

		// then
		verify(orderTableDao, times(1)).findAllByIdIn(anyList());
		verify(tableGroupDao, times(1)).save(tableGroup);
		verify(orderTableDao, times(orderTables.size())).save(any(OrderTable.class));

	}

	@Test
	@DisplayName("두개 미만의 주문 테이블로 테이블 그룹 생성")
	void testCreateMenuGroupWhenOrderTableSizeBelowThanTwo() {
		// given
		List<OrderTable> orderTables = createOrderTables(true);
		TableGroup tableGroup = createTableGroup(orderTables);

		// when
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("빈 주문 테이블로 테이블 그룹 생성")
	void testCreateMenuGroupWhenOrderTableIsEmpty() {
		// given
		List<OrderTable> orderTables = createOrderTables(false);
		TableGroup tableGroup = createTableGroup(orderTables);

		// when
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("테이블 그룹 해제")
	void testCreateMenuUnGroup() {
		// given
		List<OrderTable> orderTables = createOrderTables(true);
		TableGroup tableGroup = createTableGroup(orderTables);
		when(orderTableDao.findAllByTableGroupId(any())).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
			.thenReturn(false);

		// when
		tableGroupService.ungroup(tableGroup.getId());

		// then
		verify(orderTableDao, times(orderTables.size())).save(any());
	}

	@Test
	@DisplayName("테이블 그룹 해제시 완료되지 않은 주문이 있을 경우")
	void testCreateMenuUnGroupWhenOrderStatusNotComplete() {
		// given
		List<OrderTable> orderTables = createOrderTables(true);
		TableGroup tableGroup = createTableGroup(orderTables);
		when(orderTableDao.findAllByTableGroupId(any())).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
			.thenReturn(true);

		// when
		assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
			.isInstanceOf(IllegalArgumentException.class);

		// then
		verify(orderTableDao, times(0)).save(any());
	}

	private TableGroup createTableGroup(List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);
		tableGroup.setId(1L);
		return tableGroup;
	}

	private List<OrderTable> createOrderTables(boolean isEmpty) {
		return Lists.newArrayList(getOrderTable(1L, isEmpty), getOrderTable(2L, isEmpty));
	}

	private static OrderTable getOrderTable(long id, boolean isEmpty) {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(isEmpty);
		orderTable.setId(id);
		return orderTable;
	}
}
