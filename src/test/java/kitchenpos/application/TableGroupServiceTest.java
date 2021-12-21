package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.assertj.core.util.Lists;
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

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	@Test
	@DisplayName("테이블 그룹 생성 테스트")
	public void createTableGroupSuccessTest() {
		//given
		OrderTable orderTable = new OrderTable(1L, null, 0, true);
		OrderTable otherOrderTable = new OrderTable(2L, null, 0, true);
		TableGroup tableGroup = new TableGroup(null, null, Lists.newArrayList(orderTable, otherOrderTable));
		when(orderTableDao.findAllByIdIn(Lists.newArrayList(1L,2L))).thenReturn(Lists.newArrayList(orderTable, otherOrderTable));
		when(tableGroupDao.save(tableGroup)).thenReturn( new TableGroup(1L, LocalDateTime.now(), Lists.newArrayList(orderTable, otherOrderTable)));
		when(orderTableDao.save(orderTable)).thenReturn(new OrderTable(1L, 1L, 0, false));
		when(orderTableDao.save(otherOrderTable)).thenReturn(new OrderTable(2L, 1L, 0, false));
		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		//when
		TableGroup save = tableGroupService.create(tableGroup);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(1L);
		assertThat(save.getOrderTables()).hasSize(2);
	}

	@Test
	@DisplayName("테이블의 개수가 2개보다 작아서 그룹생성 실패")
	public void createTableGroupFailOrderTableLessThanTwoTest() {
		//given
		TableGroup tableGroup = new TableGroup(null, null, Lists.emptyList());
		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		//when
		//then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("2개 이상의 테이블만 그룹생성이 가능합니다");
	}

	@Test
	@DisplayName("존재하지 않는 테이블로 그룹생성 요청해서 실패")
	public void createTableGroupFailNotExistedTableTest() {
		//given
		OrderTable orderTable = new OrderTable(1L, null, 0, true);
		OrderTable otherOrderTable = new OrderTable(2L, null, 0, true);
		TableGroup tableGroup = new TableGroup(null, null, Lists.newArrayList(orderTable, otherOrderTable));
		when(orderTableDao.findAllByIdIn(Lists.newArrayList(1L,2L))).thenReturn(Lists.newArrayList(orderTable));
		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		//when
		//then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("존재하는 테이블만 그룹생성이 가능합니다");
	}

	@Test
	@DisplayName("사용중이거나 이미 그룹에 속한 테이블로 그룹 생성 요청해서 실패")
	public void createTableGroupFailTableUseOrAlreadyGroupingTest() {
		//given
		OrderTable orderTable = new OrderTable(1L, null, 0, false);
		OrderTable otherOrderTable = new OrderTable(2L, 1L, 0, true);
		TableGroup tableGroup = new TableGroup(null, null, Lists.newArrayList(orderTable, otherOrderTable));
		when(orderTableDao.findAllByIdIn(Lists.newArrayList(1L,2L))).thenReturn(Lists.newArrayList(orderTable,otherOrderTable));
		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		//when
		//then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 사용중이거나 그룹화된 테이블은 그룹생성 할 수 없습니다");
	}

	@Test
	@DisplayName("주문이 계산완료되지 않아서 테이블 그룹 해제 실패")
	public void ungroupFailTest() {
		//given
		OrderTable orderTable = new OrderTable(1L, 1L, 0, false);
		OrderTable otherOrderTable = new OrderTable(2L, 1L, 0, false);
		when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Lists.newArrayList(orderTable, otherOrderTable));
		ArrayList<String> orderStatus = Lists.newArrayList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.newArrayList(1L,2L), orderStatus)).thenReturn(true);
		TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

		//when
		//then
		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("아직 주문이 계산완료되지 않았습니다");
	}
}
