package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.NumberOfGuests;
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
	@InjectMocks
	private TableGroupService tableGroupService;

	private List<OrderTable> 주문테이블;
	private OrderTable 일번테이블;
	private OrderTable 이번테이블;
	private List<Long> 주문테이블아이디목록;
	private List<String> OrderStatusList;

	@BeforeEach
	void setUp() {
		주문테이블 = new ArrayList<>();
		일번테이블 = new OrderTable(null, new NumberOfGuests(1), false);
		이번테이블 = new OrderTable(null, new NumberOfGuests(2), false);

		주문테이블.add(일번테이블);
		주문테이블.add(이번테이블);

		주문테이블아이디목록 = 주문테이블.stream().map(OrderTable::getId).collect(Collectors.toList());
		OrderStatusList = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
	}

	@DisplayName("단체 지정 해제 테스트")
	@Test
	void testUnGroup() {
		Long tableGroupId = 1L;
		List<String> OrderStatusList = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

		when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(주문테이블);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(주문테이블아이디목록, OrderStatusList)).thenReturn(false);
		when(orderTableDao.save(일번테이블)).thenReturn(일번테이블);
		when(orderTableDao.save(이번테이블)).thenReturn(이번테이블);

		tableGroupService.ungroup(tableGroupId);

		verify(orderTableDao, times(1)).save(일번테이블);
		verify(orderTableDao, times(1)).save(이번테이블);
	}

	@DisplayName("단체 지정 해제시 주문 테이블이 목록에 존재하고, 상태가 COOKING 또는 MEAL인 경우 오류 발생")
	@Test
	void testUnGroupExistsByOrderTableIdInAndOrderStatusIn() {
		//given
		long tableGroupId = 1L;
		when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(주문테이블);
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(주문테이블아이디목록, OrderStatusList)).thenReturn(true);
		//when
		assertThatThrownBy(() -> {
			tableGroupService.ungroup(tableGroupId);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 상태가 완료되어야 단체지정이 해제가능합니다.");
	}

	@DisplayName("주문 테이블이 2테이블 이하인경우 단체지정 오류 발생")
	@Test
	void testOrderTablesEmpty() {
		List<OrderTable> emptyOrderTables = new ArrayList<>();
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), emptyOrderTables);

		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 테이블이이 둘 이상이어야 단체지정을 할 수 있습니다.");
	}

	@DisplayName("주문 테이블과 저장된 주문 테이블의 크기가 다르면 오류 발생")
	@Test
	void testNotEqualsOrderTableSize() {
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), 주문테이블);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), false));
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), false));
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), false));

		when(orderTableDao.findAllByIdIn(주문테이블아이디목록)).thenReturn(savedOrderTables);

		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 테이블과 저장된 주문 테이블의 갯수가 다릅니다.");
	}

	@DisplayName("저장 되어있는 주문 테이블이 테이블 그룹이 없으면 오류 발생")
	@Test
	void testOrderTableNotSetTableGroup() {
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), 주문테이블);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), false));
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), false));

		when(orderTableDao.findAllByIdIn(주문테이블아이디목록)).thenReturn(savedOrderTables);

		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블이 단체지정이 되어있거나, 비어있지 않은 테이블입니다.");
	}

	@DisplayName("저장 되어있는 주문 테이블이 비어있는 테이블이 아니면 오류 발생")
	@Test
	void testOrderTableIsEmpty() {
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), 주문테이블);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), false));
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), false));

		when(orderTableDao.findAllByIdIn(주문테이블아이디목록)).thenReturn(savedOrderTables);

		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블이 단체지정이 되어있거나, 비어있지 않은 테이블입니다.");
	}

	@DisplayName("단체 지정 테스트")
	@Test
	void testCreateTableGroup() {
		LocalDateTime createdDate = LocalDateTime.of(2021, 7, 6, 0, 0, 0);
		TableGroup tableGroup = new TableGroup(1L, createdDate, 주문테이블);

		List<OrderTable> savedOrderTables = new ArrayList<>();
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), true));
		savedOrderTables.add(new OrderTable(null, new NumberOfGuests(3), true));
		TableGroup savedTableGroup = new TableGroup(1L, createdDate, savedOrderTables);

		when(orderTableDao.findAllByIdIn(주문테이블아이디목록)).thenReturn(savedOrderTables);
		when(tableGroupDao.save(tableGroup)).thenReturn(savedTableGroup);

		TableGroup actual = tableGroupService.create(tableGroup);

		assertThat(actual.getOrderTables()).containsExactlyElementsOf(savedOrderTables);
	}
}