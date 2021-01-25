package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.orders.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@DisplayName("단체")
class TableGroupServiceTest extends IntegrationTest {

	@Autowired
	private TableGroupService tableGroupService;
	@Autowired
	private OrderTableDao orderTableDao;
	@Autowired
	private OrderDao orderDao;

	@DisplayName("단체를 지정할 수 있다.")
	@Test
	void create() {
		// given
		OrderTable orderTable1 = new OrderTable(0, true);
		OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

		OrderTable orderTable2 = new OrderTable(0, true);
		OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

		// when
		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
		TableGroupResponse finalSavedTableGroup = tableGroupService.create(request);

		// then
		assertThat(finalSavedTableGroup.getId()).isNotNull();

		List<Long> actualOrderTableIds = finalSavedTableGroup.getOrderTables().stream()
			.map(OrderTableResponse::getId)
			.collect(Collectors.toList());
		assertThat(actualOrderTableIds).containsExactly(orderTable1.getId(), orderTable2.getId());
	}

	@DisplayName("테이블은 2개 이상일 경우에만 지정할 수 있다.")
	@Test
	void tableCountMustOverTwice() {
		// given
		OrderTable orderTable = new OrderTable(0, true);
		OrderTable savedOrderTable = orderTableDao.save(orderTable);

		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable.getId()));
		// when
		assertThatThrownBy(() -> {
			tableGroupService.create(request);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문한 테이블들이 실제로 존재하지 않는 경우 단체로 지정할 수 없다.")
	@Test
	void requestedOrderTableMustExist() {
		// when - then
		TableGroupRequest request = new TableGroupRequest(Arrays.asList(22222L, 333333L));
		assertThatThrownBy(() -> {
			tableGroupService.create(request);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("요청된 테이블들이 빈 테이블이 아니면 단체로 지정할 수 없다.")
	@Test
	void requestedOrderTableMustEmpty() {
		// given
		OrderTable orderTable1 = new OrderTable(0, false);
		OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

		OrderTable orderTable2 = new OrderTable(0, false);
		OrderTable saveOrderTable2 = orderTableDao.save(orderTable2);

		// when

		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.getId(), saveOrderTable2.getId()));
		// when - then
		assertThatThrownBy(() -> {
			tableGroupService.create(request);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체를 해제할 수 있다.")
	@Test
	void ungroup(){
		// given
		OrderTable orderTable1 = new OrderTable(0, true);
		OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

		OrderTable orderTable2 = new OrderTable(0, true);
		OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
		TableGroupResponse finalSavedTableGroup = tableGroupService.create(request);

		// when
		tableGroupService.ungroup(finalSavedTableGroup.getId());

		// then
		List<OrderTable> actualOrderTables = orderTableDao.findAllByTableGroupId(finalSavedTableGroup.getId());

		List<Long> actualOrderTableIds = actualOrderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		assertThat(actualOrderTableIds).doesNotContain(finalSavedTableGroup.getId());

	}

	@DisplayName("테이블중 요리중이거나 식사중인 상태인 경우 단체를 해제할 수 없다.")
	@Test
	void cookingOrMealCannotCreateTableGroup() {
		// given
		OrderTable orderTable1 = new OrderTable(0, true);
		OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

		OrderTable orderTable2 = new OrderTable(0, true);
		OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

		TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId()));
		TableGroupResponse savedTableGroup = tableGroupService.create(request);

		orderDao.save(new Orders(savedOrderTable1.getId(), OrderStatus.MEAL.name()));

		// when - then
		assertThatThrownBy(() -> {
			tableGroupService.ungroup(savedTableGroup.getId());
		}).isInstanceOf(IllegalArgumentException.class);
	}
}