package kitchenpos.application;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
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
	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("단체 지정 해제 테스트")
	@Test
	void testUnGroup() {
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(1L, 1L, 3, false));
		orderTables.add(new OrderTable(2L, 1L, 3, false));

		when(orderTableDao.findAllByTableGroupId(eq(1L))).thenReturn(orderTables);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Mockito.anyList(), Mockito.anyList())).thenReturn(false);

		tableGroupService.ungroup(1L);

		verify(orderTableDao, times(2)).save(Mockito.any());
	}

	@DisplayName("단체 지정 해제시 주문 테이블이 목록에 존재하고, 상태가 COOKING 또는 MEAL인 경우 오류 발생")
	@Test
	void testUnGroupExistsByOrderTableIdInAndOrderStatusIn() {
		//given
		when(orderTableDao.findAllByTableGroupId(eq(1L))).thenReturn(Collections.emptyList());
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Mockito.anyList(), Mockito.anyList())).thenReturn(true);
		//when
		Assertions.assertThatThrownBy(() -> {
			tableGroupService.ungroup(1L);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 상태가 완료되어야 단체지정이 해제가능합니다.");
	}

	@DisplayName("주문 테이블이 2테이블 이하인경우 단체지정 오류 발생")
	@Test
	void testOrderTablesEmpty() {
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(1L, 1L, 3, false));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		Assertions.assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 테이블이이 둘 이상이어야 단체지정을 할 수 있습니다.");
	}

	@DisplayName("주문 테이블과 저장된 주문 테이블의 크기가 다르면 오류 발생")
	@Test
	void testNotEqualsOrderTableSize() {
		//given
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(1L, 1L, 3, false));
		orderTables.add(new OrderTable(2L, 1L, 3, false));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(1L, 1L, 3, false));
		savedOrderTables.add(new OrderTable(2L, 1L, 3, false));
		savedOrderTables.add(new OrderTable(3L, 1L, 3, false));

		//when
		Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(savedOrderTables);

		Assertions.assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 테이블과 저장된 주문 테이블의 갯수가 다릅니다.");
	}

	@DisplayName("저장 되어있는 주문 테이블이 테이블 그룹이 없으면 오류 발생")
	@Test
	void testOrderTableNotSetTableGroup() {
		//given
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(1L, 1L, 3, false));
		orderTables.add(new OrderTable(2L, 1L, 3, false));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(1L, 1L, 3, false));
		savedOrderTables.add(new OrderTable(2L, null, 3, false));

		//when
		Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(savedOrderTables);

		Assertions.assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블의 테이블 그룹이 없거나, 빈 테이블입니다.");
	}

	@DisplayName("저장 되어있는 주문 테이블이 테이블 그룹이 없으면 오류 발생")
	@Test
	void testOrderTableIsEmpty() {
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(1L, 1L, 3, false));
		orderTables.add(new OrderTable(2L, 1L, 3, false));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(1L, 1L, 3, false));
		savedOrderTables.add(new OrderTable(2L, 1L, 3, true));

		Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(savedOrderTables);

		Assertions.assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블의 테이블 그룹이 없거나, 빈 테이블입니다.");
	}

	@DisplayName("단체 지정 테스트")
	@Test
	void testCreateTableGroup() {
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(1L, 1L, 3, false));
		orderTables.add(new OrderTable(2L, 1L, 3, false));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(1L, null, 3, true));
		savedOrderTables.add(new OrderTable(2L, null, 3, true));

		Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList())).thenReturn(savedOrderTables);

		TableGroup savedTableGroup = Mockito.mock(TableGroup.class);
		Mockito.when(tableGroupDao.save(Mockito.any())).thenReturn(savedTableGroup);

		tableGroupService.create(tableGroup);
		Mockito.verify(orderTableDao, times(2)).save(Mockito.any());
	}

}