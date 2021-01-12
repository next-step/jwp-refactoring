package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.MySpringBootTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@MySpringBootTest
class TableGroupServiceTest {

	@Autowired
	private TableGroupService tableGroupService;
	@Autowired
	private OrderTableDao orderTableDao;
	@Autowired
	private TableGroupDao tableGroupDao;
	@MockBean
	private OrderDao orderDao;

	@DisplayName("단체 지정 정보를 등록한다.")
	@Test
	void create() {
		OrderTable orderTable1 = orderTableDao.findById(1L).get();
		OrderTable orderTable2 = orderTableDao.findById(2L).get();

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

		//when
		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		//then
		단체지정정보가_등록됨(savedTableGroup);
		주문_테이블이_단체지정_테이블로_변경됨(savedTableGroup);
	}

	@DisplayName("주문 테이블이 2개 미만인 경우 단체 지정을 할 수 없다.")
	@Test
	void creatWithOneOrderTable() {
		OrderTable orderTable1 = orderTableDao.findById(1L).get();

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable1));

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("등록된 주문 테이블이 아닌 경우 단체 지정을 할 수 없다.")
	@Test
	void creatWithNotExistOrderTable() {
		OrderTable orderTable1 = orderTableDao.findById(1L).get();
		OrderTable orderTable2 = new OrderTable();
		orderTable2.setId(0L);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("빈 테이블이 아닌 경우 단체 지정을 할 수 없다.")
	@Test
	void creatWithNotEmptyOrderTable() {
		OrderTable orderTable1 = orderTableDao.findById(1L).get();
		OrderTable orderTable2 = orderTableDao.findById(11L).get();
		OrderTable inOtherTableGroup = orderTableDao.findById(12L).get();

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> tableGroupService.create(tableGroup));

		//when, then
		tableGroup.setOrderTables(Arrays.asList(orderTable1, inOtherTableGroup));
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("단체지정을 해제한다.")
	@Test
	void ungroup() {
		TableGroup tableGroup = tableGroupDao.findById(1L).get();

		//when
		tableGroupService.ungroup(tableGroup.getId());
		//then
		List<OrderTable> orderTables = orderTableDao
			  .findAllByTableGroupId(tableGroup.getId());
		assertThat(orderTables).isEmpty();
	}

	@DisplayName("조리, 식사중인 테이블인 경우 단체지정을 해제할 수 없다.")
	@Test
	void ungroupWithEatingTable() {
		TableGroup tableGroup = tableGroupDao.findById(1L).get();
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			  any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))))
			  .thenReturn(true);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
	}

	private void 단체지정정보가_등록됨(TableGroup savedTableGroup) {
		Optional<TableGroup> maybeTableGroup = tableGroupDao.findById(savedTableGroup.getId());
		assertThat(maybeTableGroup.isPresent()).isTrue();
	}

	private void 주문_테이블이_단체지정_테이블로_변경됨(TableGroup savedTableGroup) {
		List<OrderTable> orderTables = orderTableDao
			  .findAllByTableGroupId(savedTableGroup.getId());
		assertThat(orderTables).hasSize(2);
		orderTables.forEach(orderTable -> assertThat(orderTable.isEmpty()).isFalse());
	}
}
