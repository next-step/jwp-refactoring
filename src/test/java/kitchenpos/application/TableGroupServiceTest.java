package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

	private OrderTable orderTable1;
	private OrderTable orderTable2;
	private List<OrderTable> orderTables;

	@BeforeEach
	void setUp() {
		orderTable1 = OrderTable.of(1L, null, 0, true);
		orderTable2 = OrderTable.of(2L, null, 0, true);

		orderTables = Arrays.asList(orderTable1, orderTable2);
	}

	@DisplayName("단체: 단체 생성 테스트")
	@Test
	void createTest() {
		// given
		TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);
		given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
		given(tableGroupDao.save(any())).willReturn(tableGroup);
		given(orderTableDao.save(any())).willReturn(orderTable1, orderTable2);

		// when
		final TableGroup actual = tableGroupService.create(tableGroup);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getOrderTables().stream()
				.filter(OrderTable::isEmpty)
				.count()).isEqualTo(0)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(1. 주문 테이블이 2개 이상이여야 한다.)")
	@Test
	void errorTableGroupForNullTable() {
		// given // when
		TableGroup tableGroup = TableGroup.of(1L, null, null);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(tableGroup)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(1. 주문 테이블이 2개 이상이여야 한다.)")
	@Test
	void errorTableGroupForOneTable() {
		// given //when
		TableGroup tableGroup = TableGroup.of(1L, null, Collections.singletonList(mock(OrderTable.class)));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(tableGroup)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(2. 주문 테이블이 모두 등록되 있어야 한다.)")
	@Test
	void createTableGroupForNotExistTable() {
		// given
		TableGroup tableGroup = TableGroup.of(1L, null, orderTables);

		// when
		when(orderTableDao.findAllByIdIn(any())).thenReturn(new ArrayList<>());

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(tableGroup)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(3. 주문 테이블이 빈 테이블이여야 한다.)")
	@Test
	void createTableGroupForNotEmptyTable() {
		// given
		TableGroup tableGroup = TableGroup.of(1L, null, orderTables);
		OrderTable notEmptyTable = OrderTable.of(1L, null, 0, false);

		// when
		when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(notEmptyTable, mock(OrderTable.class)));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(tableGroup)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(3. 주문 테이블이 그룹에 포함되지 않아야 한다.)")
	@Test
	void createTableGroupForAlreadyHaveGroup() {
		// given
		TableGroup tableGroup = TableGroup.of(1L, null, orderTables);
		OrderTable existGroupTable = OrderTable.of(1L, 3L, 0, false);

		// when
		when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(existGroupTable, mock(OrderTable.class)));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(tableGroup)
		);
	}

	@DisplayName("단체: 단체 해지 테스트")
	@Test
	void ungroup() {
		// given
		given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

		// when
		tableGroupService.ungroup(1L);

		// then
		assertThat(orderTables.stream()
			.filter(table -> table.getTableGroupId() == null)
			.collect(Collectors.toList())).containsExactlyInAnyOrder(orderTable1, orderTable2);
	}

	@DisplayName("단체[예외]: 단체 해지 테스트(주문 상태가 조리 또는 식사인 테이블은 해지가 불가능하다.)")
	@Test
	void ungroupCookingTable() {
		given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

		//when-then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.ungroup(1L)
		);
	}
}
