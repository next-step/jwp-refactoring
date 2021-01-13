package kitchenpos.application;

import kitchenpos.MockitoTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class TableGroupServiceTest extends MockitoTest {

	@InjectMocks
	private TableGroupService tableGroupService;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	private OrderTable orderTable1;
	private OrderTable orderTable2;
	private OrderTable orderTable3;
	private TableGroup tableGroup;
	private List<OrderTable> orderTables;

	@BeforeEach
	void setUp() {
		orderTable1 = MockFixture.orderTable(1L, null, true, 10);
		orderTable2 = MockFixture.orderTable(2L, null, true, 2);
		orderTable3 = MockFixture.orderTable(3L, null, true, 7);

		orderTables = Arrays.asList(orderTable1, orderTable2, orderTable3);
		tableGroup = MockFixture.tableGroupForCreate(orderTables);
	}

	@DisplayName("여러 개의 테이블을 묶어 단체 지정한다.")
	@Test
	void create() {
		// given
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
		given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

		// when
		tableGroup = tableGroupService.create(tableGroup);

		// then
		verify(tableGroupDao).save(tableGroup);
		orderTables.forEach(orderTable -> verify(orderTable).setTableGroupId(anyLong()));
	}

	@DisplayName("단체 지정하려는 테이블 수가 적을 경우 예외 발생.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1})
	void create_OrderTablesLow(int size) {
		List<OrderTable> orderTables = MockFixture.anyOrderTables(size);
		final TableGroup tableGroup = MockFixture.tableGroupForCreate(orderTables);
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
		given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("이미 단체 지정된 테이블을 단체 지정하려고 시도 할 경우 예외 발생.")
	@Test
	void create_AlreadyGroupExist() {
		final TableGroup tableGroup = MockFixture.tableGroupForCreate(orderTables);
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
		given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정시 테이블이 비어 있지 않은 경우 예외 발생.")
	@Test
	void create_EmptyFalse() {
		given(orderTable1.isEmpty()).willReturn(false);
		final TableGroup tableGroup = MockFixture.tableGroupForCreate(orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정된 테이블의 단체지정을 해제한다.")
	@Test
	void ungroup() {
		// given
		given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

		// when
		tableGroupService.ungroup(1L);

		// then
		verify(orderTable1).setTableGroupId(isNull());
		verify(orderTable2).setTableGroupId(isNull());
		verify(orderTable3).setTableGroupId(isNull());
		verify(orderTableDao).save(orderTable1);
		verify(orderTableDao).save(orderTable2);
		verify(orderTableDao).save(orderTable3);
	}

	@DisplayName("")
	@Test
	void ungroup_StatusWrong() {
		// given
		given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

		// when then
		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
				.isInstanceOf(IllegalArgumentException.class);

	}
}
