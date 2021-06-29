package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("주문테이블 그룹 요구사항 테스트")
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

	@DisplayName("2개 이상의 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithLessTwoOrderTablesTest() {
		// given
		TableGroup tableGroup = mock(TableGroup.class);
		when(tableGroup.getOrderTables()).thenReturn(new ArrayList<>());

		// when
		// then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블이 2개 이하입니다.");
	}

	@DisplayName("등록된 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithUnknownOrderTableTest() {
		// given
		OrderTable orderTable1 = mock(OrderTable.class);
		OrderTable orderTable2 = mock(OrderTable.class);
		TableGroup tableGroup = mock(TableGroup.class);
		when(tableGroup.getOrderTables()).thenReturn(asList(orderTable1, orderTable2));

		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(new ArrayList<>());

		// when
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록된 주문테이블로만 그룹화 할 수 있습니다.");
	}

	@DisplayName("그룹화할 주문테이블들 모두 빈 테이블이어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		// given
		OrderTable emptyOrderTable = mock(OrderTable.class);
		when(emptyOrderTable.isEmpty()).thenReturn(true);
		OrderTable notEmptyOrderTable = mock(OrderTable.class);
		TableGroup tableGroup = mock(TableGroup.class);
		when(tableGroup.getOrderTables()).thenReturn(asList(emptyOrderTable, notEmptyOrderTable));

		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(asList(emptyOrderTable, notEmptyOrderTable));

		// when
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("그룹화할 주문테이블들 모두 빈 테이블이어야 합니다.");
	}

	@DisplayName("그룹화된 주문테이블들 중 조리상태이거나 식사상태이면 그룹해제를 할 수 없다.")
	@Test
	void createTableGroupTest() {
		// given
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);
		// when
		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("그룹화된 주문테이블 중 조리상태이거나 식사상태가 있습니다.");
	}

}