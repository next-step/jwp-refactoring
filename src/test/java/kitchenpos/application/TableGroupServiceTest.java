package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	OrderDao orderDao;
	@Mock
	OrderTableDao orderTableDao;
	@Mock
	TableGroupDao tableGroupDao;

	@DisplayName("단체를 지정할 수 있다.")
	@Test
	void create() {
		// given

		// given(request)
		TableGroup tableGroup = mock(TableGroup.class);
		OrderTable orderTable1 = mock(OrderTable.class);
		when(orderTable1.getId()).thenReturn(1L);

		OrderTable orderTable2 = mock(OrderTable.class);
		when(orderTable2.getId()).thenReturn(2L);
		when(tableGroup.getOrderTables()).thenReturn(Arrays.asList(orderTable1, orderTable2));

		// given(request saved)
		OrderTable savedOrderTable1 = mock(OrderTable.class);
		when(savedOrderTable1.isEmpty()).thenReturn(true);
		when(savedOrderTable1.getTableGroupId()).thenReturn(null);

		OrderTable savedOrderTable2 = mock(OrderTable.class);
		when(savedOrderTable2.isEmpty()).thenReturn(true);
		when(savedOrderTable2.getTableGroupId()).thenReturn(null);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.addAll(Arrays.asList(savedOrderTable1, savedOrderTable2));
		when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(savedOrderTables);

		// given(expected)
		TableGroup savedTableGroup = mock(TableGroup.class);
		when(savedTableGroup.getId()).thenReturn(1L);

		when(tableGroupDao.save(tableGroup)).thenReturn(savedTableGroup);

		OrderTable expectedOrderTable1 = mock(OrderTable.class);
		OrderTable expectedOrderTable2 = mock(OrderTable.class);

		when(savedTableGroup.getOrderTables()).thenReturn(Arrays.asList(expectedOrderTable1, expectedOrderTable2));

		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		// when
		TableGroup finalSavedTableGroup = tableGroupService.create(tableGroup);

		// then
		assertThat(finalSavedTableGroup.getId()).isNotNull();
		assertThat(finalSavedTableGroup.getOrderTables()).containsExactly(expectedOrderTable1, expectedOrderTable2);
	}

	@DisplayName("테이블은 2개 이상일 경우에만 지정할 수 있다.")
	@Test
	void tableCountMustOverTwice() {
		// given
		TableGroup tableGroup = mock(TableGroup.class);

		OrderTable orderTable1 = mock(OrderTable.class);
		when(tableGroup.getOrderTables()).thenReturn(Arrays.asList(orderTable1));
		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		// when
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문한 테이블들이 실제로 존재하지 않는 경우 단체로 지정할 수 없다.")
	@Test
	void requestedOrderTableMustExist() {
		//given
		TableGroup tableGroup = mock(TableGroup.class);

		OrderTable orderTable1 = mock(OrderTable.class);
		when(orderTable1.getId()).thenReturn(1L);

		OrderTable orderTable2 = mock(OrderTable.class);
		when(orderTable2.getId()).thenReturn(2L);
		when(tableGroup.getOrderTables()).thenReturn(Arrays.asList(orderTable1, orderTable2));

		OrderTable realOrderTable = mock(OrderTable.class);
		when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(realOrderTable));
		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		// when - then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요청된 테이블들이 빈 테이블이 아니면 단체로 지정할 수 없다.")
	@Test
	void requestedOrderTableMustEmpty() {
		// given - reqeust
		TableGroup tableGroup = mock(TableGroup.class);

		OrderTable orderTable1 = mock(OrderTable.class);
		when(orderTable1.getId()).thenReturn(1L);

		OrderTable orderTable2 = mock(OrderTable.class);
		when(orderTable2.getId()).thenReturn(2L);
		when(tableGroup.getOrderTables()).thenReturn(Arrays.asList(orderTable1, orderTable2));

		// given - request saved
		OrderTable savedOrderTable1 = mock(OrderTable.class);
		when(savedOrderTable1.isEmpty()).thenReturn(false);

		OrderTable savedOrderTable2 = mock(OrderTable.class);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.addAll(Arrays.asList(savedOrderTable1, savedOrderTable2));
		when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(savedOrderTables);

		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		// when - then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체를 해제할 수 있다.")
	@Test
	void ungroup(){
		// given
		Long tableGroupId = 1L;
		TableGroup tableGroup = mock(TableGroup.class);

		OrderTable orderTable1 = mock(OrderTable.class);
		when(orderTable1.getId()).thenReturn(1L);
		OrderTable orderTable2 = mock(OrderTable.class);
		when(orderTable2.getId()).thenReturn(2L);

		when(tableGroup.getId()).thenReturn(tableGroupId);
		orderTable1.setTableGroupId(tableGroupId);
		orderTable2.setTableGroupId(tableGroupId);

		when(orderTableDao.findAllByTableGroupId(tableGroup.getId())).thenReturn(Arrays.asList(orderTable1, orderTable2));
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

		OrderTable expectedOrderTable1 = mock(OrderTable.class);
		when(expectedOrderTable1.getTableGroupId()).thenReturn(null);
		OrderTable expectedOrderTable2 = mock(OrderTable.class);
		when(expectedOrderTable2.getTableGroupId()).thenReturn(null);

		when(orderTableDao.save(orderTable1)).thenReturn(expectedOrderTable1);
		when(orderTableDao.save(orderTable2)).thenReturn(expectedOrderTable2);

		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		// when
		tableGroupService.ungroup(tableGroupId);

		// then
		assertThat(expectedOrderTable1.getTableGroupId()).isNull();
		assertThat(expectedOrderTable2.getTableGroupId()).isNull();

	}



	@DisplayName("테이블중 요리중이거나 식사중인 상태인 경우 단체를 해제할 수 없다.")
	@Test
	void cookingOrMealCannotCreateTableGroup() {
		// given
		Long tableGroupId = 1L;

		OrderTable orderTable1 = mock(OrderTable.class);
		when(orderTable1.getId()).thenReturn(1L);

		when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(Arrays.asList(orderTable1));
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId()),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		// when - then
		assertThatThrownBy(() -> {
			tableGroupService.ungroup(tableGroupId);
		}).isInstanceOf(IllegalArgumentException.class);
	}
}