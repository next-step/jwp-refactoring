package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

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
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("단체 지정 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	private TableGroup tableGroup;
	private List<OrderTable> orderTables;
	private OrderTable orderTable1;
	private OrderTable orderTable2;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	@InjectMocks
	private TableGroupService tableGroupService;

	@BeforeEach
	void setup() {
		tableGroup = new TableGroup();
		tableGroup.setId(1L);

		orderTables = new ArrayList<>();

		orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTables.add(orderTable1);

		orderTable2 = new OrderTable();
		orderTable2.setId(2L);
		orderTable2.setEmpty(true);
		orderTables.add(orderTable2);

		tableGroup.setOrderTables(orderTables);
	}

	@DisplayName("2 개 이상의 빈 테이블을 단체로 지정할 수 있다.")
	@Test
	void create() {
		// given
		given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
		given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
		given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
		given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

		// when
		TableGroup createdTableGroup = tableGroupService.create(tableGroup);

		// then
		assertThat(createdTableGroup.getId()).isEqualTo(this.tableGroup.getId());
	}

	@DisplayName("단체 지정은 중복될 수 없다.")
	@Test
	void createNoDuplicate() {
		orderTable1.setTableGroupId(1L);

		assertThrows(IllegalArgumentException.class, () -> {
			tableGroupService.create(tableGroup);
		});
	}

	@DisplayName("주문 테이블 상태가 비어있지 않으면 등록할 수 없다.")
	@Test
	void createEmpty() {
		orderTable1.setEmpty(false);

		assertThrows(IllegalArgumentException.class, () -> {
			tableGroupService.create(tableGroup);
		});
	}

	@DisplayName("단체 지정을 해지할 수 있다.")
	@Test
	void unGroup() {
		orderTable1.setTableGroupId(tableGroup.getId());
		orderTable2.setTableGroupId(tableGroup.getId());

		given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
			.willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(),any()))
			.willReturn(false);
		given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
		given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

		// when
		tableGroupService.ungroup(tableGroup.getId());

		// then
		assertThat(orderTable1.getTableGroupId()).isNull();
		assertThat(orderTable2.getTableGroupId()).isNull();
	}

	@DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
	@Test
	void unGroupNotChangeStatus() {
		orderTable1.setTableGroupId(tableGroup.getId());
		orderTable2.setTableGroupId(tableGroup.getId());

		given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
			.willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(),any()))
			.willReturn(true);

		assertThrows(IllegalArgumentException.class, () -> {
			tableGroupService.ungroup(tableGroup.getId());
		});
	}
}
