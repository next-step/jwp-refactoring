package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.TableGroupService;

@DisplayName("단체 지정 BO 테스트")
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

	@DisplayName("단체 지정")
	@Test
	void create_happyPath() {
		// given
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블1, 주문_테이블2).build();
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
		given(tableGroupDao.save(단체_지정)).willAnswer(invocation -> {
			단체_지정.setId(1L);
			return 단체_지정;
		});

		// when
		TableGroup savetableGroup = tableGroupService.create(단체_지정);

		// then
		assertThat(savetableGroup).isEqualTo(단체_지정);
		assertThat(savetableGroup.getOrderTables()).contains(주문_테이블1, 주문_테이블2);
		assertThat(savetableGroup.getOrderTables()).map(OrderTable::isEmpty).containsExactly(false, false);
	}

	@DisplayName("단체 지정 : 문 테이블들이 이미 단체 지정 되어있음")
	@Test
	void create_exceptionCase1() {
		// given
		TableGroup 이미_단체_지정 = new TableGroup.Builder().id(-1L).build();
		OrderTable 주문_테이블9 = new OrderTable.Builder().id(9L).tableGroup(이미_단체_지정).empty(true).build();
		OrderTable 주문_테이블10 = new OrderTable.Builder().id(10L).tableGroup(이미_단체_지정).empty(true).build();
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블9, 주문_테이블10).build();
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블9, 주문_테이블10));

		// when & then
		assertThatThrownBy(() -> tableGroupService.create(단체_지정)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정 : 주문 테이블들은 빈 테이블이 아님")
	@Test
	void create_exceptionCase2() {
		// given
		OrderTable 주문_테이블9 = new OrderTable.Builder().id(9L).empty(false).build();
		OrderTable 주문_테이블10 = new OrderTable.Builder().id(10L).empty(false).build();
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블9, 주문_테이블10).build();
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블9, 주문_테이블10));

		// when & then
		assertThatThrownBy(() -> tableGroupService.create(단체_지정)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 해제")
	@Test
	void ungroup() {
		// given
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블1, 주문_테이블2).build();

		given(orderTableDao.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			anyList(),
			eq(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
		).willReturn(false);

		// when
		tableGroupService.ungroup(단체_지정.getId());

		// then
		assertThat(주문_테이블1.getTableGroup()).isNull();
		assertThat(주문_테이블2.getTableGroup()).isNull();
	}

	@DisplayName("단체 해제 : 단체 지정되어 있던 주문 테이블의 주문 상태가 계산 완료가 아닌 것이 존재함")
	@Test
	void ungroup_exceptionCase() {
		// given
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블1, 주문_테이블2).build();
		
		given(orderTableDao.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			anyList(),
			eq(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
		).willReturn(true);

		// when & then
		assertThatThrownBy(() -> tableGroupService.ungroup(단체_지정.getId())).isInstanceOf(IllegalArgumentException.class);
	}
}
