package kitchenpos.domain;

import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TableGroupServiceTest {

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	private TableGroupService tableGroupService;

	@BeforeEach
	void setUp() {
		tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
		assertThat(tableGroupService).isNotNull();
	}

	@Test
	@DisplayName("테이블 그룹을 저장한다")
	void create() {
		//- 테이블 그룹을 저장한다.
//    - 테이블이 2개 이상 묶여야함
		TableGroup tableGroup = new TableGroup();
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(true);
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTables.add(orderTable);
		orderTables.add(orderTable2);
		tableGroup.setOrderTables(orderTables);

		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);
		when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

		assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);
	}

	@Test
	@DisplayName("테이블 그룹을 저장 시 테이블은 2개 이상이어야함")
	void whenSaveTableGroupHaveToUpperTwoTables() {
		TableGroup tableGroup = new TableGroup();
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(true);
		orderTables.add(orderTable);
		tableGroup.setOrderTables(orderTables);
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(orderTables);
		assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
	}

	@Test
	@DisplayName("테이블 그룹을 삭제한다")
	void ungroup() {
		OrderTable test = mock(OrderTable.class);
		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(test));
		when(orderTableDao.save(test)).thenReturn(test);
		when(orderTableDao.findAllByTableGroupId(any())).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);
		tableGroupService.ungroup(1L);
		verify(orderTableDao, times(1)).save(test);
	}

	@Test
	@DisplayName("조리 식사 상태의 테이블은 삭제할 수 없다")
	void givenMealStatusWhenUngroupThenError조리_식사_상태의_테이블은_삭제할_수_없다() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTables.add(orderTable);
		when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(1L));
	}

}
