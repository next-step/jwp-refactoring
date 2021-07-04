package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@SpringBootTest
class TableGroupServiceTest {
	@Autowired
	private TableService tableService;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderTableDao orderTableDao;

	@Autowired
	private TableGroupService tableGroupService;

	@Test
	public void 주문테이블그룹_생성_성공() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable1 = 주문테이블생성(2);
		OrderTable orderTable2 = 주문테이블생성(2);
		orderTables.add(orderTable1);
		orderTables.add(orderTable2);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);

		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		assertThat(savedTableGroup.getOrderTables())
				.extracting("numberOfGuests", "tableGroupId", "empty")
				.contains(
						tuple(orderTable1.getNumberOfGuests(), savedTableGroup.getId(), Boolean.FALSE)
						, tuple(orderTable2.getNumberOfGuests(), savedTableGroup.getId(), Boolean.FALSE)
				);
	}

	@Test
	public void 주문테이블그룹_생성_실패_주문테이블이_두개이상이여야한다_주문테이블한개() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable1 = 주문테이블생성(2);
		orderTables.add(orderTable1);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 주문테이블그룹_생성_실패_주문테이블이_두개이상이여야한다_빈주문테이블() {
		List<OrderTable> orderTables = new ArrayList<>();

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 주문테이블그룹_생성_실패_주문테이블이_주문등록할_수_없는_상태여야한다() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable1 = 주문테이블생성(2);
		orderTable1.setEmpty(false);

		OrderTable orderTable2 = 주문테이블생성(2);

		orderTables.add(orderTable1);
		orderTables.add(orderTable2);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);

		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 주문테이블그룹_해제_성공() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable1 = 주문테이블생성(2);
		OrderTable orderTable2 = 주문테이블생성(2);
		orderTables.add(orderTable1);
		orderTables.add(orderTable2);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);

		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		tableGroupService.ungroup(savedTableGroup.getId());

		final List<Long> orderTableIds = savedTableGroup.getOrderTables().stream()
				.map(OrderTable::getId)
				.collect(Collectors.toList());

		List<OrderTable> updatedTableGroups = orderTableDao.findAllByIdIn(orderTableIds);

		assertThat(updatedTableGroups)
				.extracting("numberOfGuests", "tableGroupId", "empty")
				.contains(
						tuple(orderTable1.getNumberOfGuests(), null, Boolean.FALSE)
						, tuple(orderTable2.getNumberOfGuests(), null, Boolean.FALSE)
				);
	}

	//	@Test TODO with Order Test
	public void 주문테이블그룹_해제_실패_주문상태가_COOKING_이면_변경안됨() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable1 = 주문테이블생성(2);
		OrderTable orderTable2 = 주문테이블생성(2);
		orderTables.add(orderTable1);
		orderTables.add(orderTable2);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);

		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
		.isInstanceOf(IllegalArgumentException.class);
	}

//	@Test TODO with Order Test
	public void 주문테이블그룹_해제_실패_주문상태가_MEAL_이면_변경안됨() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable1 = 주문테이블생성(2);
		OrderTable orderTable2 = 주문테이블생성(2);
		orderTables.add(orderTable1);
		orderTables.add(orderTable2);

		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);

		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	private OrderTable 주문테이블생성(Integer numberOfGuest) {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(numberOfGuest);
		orderTable.setEmpty(true);

		return tableService.create(orderTable);
	}
}