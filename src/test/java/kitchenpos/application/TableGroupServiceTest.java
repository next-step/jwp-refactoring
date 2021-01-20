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
		when(orderTableDao.findAllByIdIn(Arrays.asList(1L,2L))).thenReturn(savedOrderTables);

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
}