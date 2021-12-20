package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
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
	private TableGroupDao tableGroupDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private OrderDao orderDao;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("테이블 그룹을 등록할 수 있다.")
	@Test
	void create() {
		//given
		LocalDateTime createdDate = LocalDateTime.now();
		List<OrderTable> orderTables = Arrays.asList(
			new OrderTable(1L,6, true),
			new OrderTable(2L,3, true)
		);
		TableGroup tableGroup = new TableGroup(createdDate, orderTables);

		given(orderTableDao.findAllByIdIn(anyList()))
			.willReturn(orderTables);
		given(tableGroupDao.save(any()))
			.willReturn(tableGroup);

		//when
		TableGroup savedTableGroup = tableGroupService.create(tableGroup);

		//then
		assertThat(savedTableGroup.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
		assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(tableGroup.getOrderTables().size());
	}

	@DisplayName("주문 테이블 리스트는 2개 이상이어야 한다.")
	@Test
	void create_exception1() {
		//given
		LocalDateTime createdDate = LocalDateTime.now();
		List<OrderTable> orderTables = Arrays.asList(
			new OrderTable(1L,6, true)
		);
		TableGroup tableGroup = new TableGroup(createdDate, orderTables);

		//when, then
		assertThatThrownBy(()->tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("모든 주문 테이블은 빈 테이블이어야 한다.")
	@Test
	void create_exception2() {
		//given
		LocalDateTime createdDate = LocalDateTime.now();
		List<OrderTable> orderTables = Arrays.asList(
			new OrderTable(1L, 6, true),
			new OrderTable(2L,3, false)
		);
		TableGroup tableGroup = new TableGroup(createdDate, orderTables);

		given(orderTableDao.findAllByIdIn(anyList()))
			.willReturn(orderTables);

		//when, then
		assertThatThrownBy(()->tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹에 속해있는 주문테이블이 포함되면 안된다.")
	@Test
	void create_exception3() {
		//given
		LocalDateTime createdDate = LocalDateTime.now();
		List<OrderTable> orderTables = Arrays.asList(
			new OrderTable(1L,6, true),
			new OrderTable(2L,5L, 3, true)
		);
		TableGroup tableGroup = new TableGroup(createdDate, orderTables);

		given(orderTableDao.findAllByIdIn(anyList()))
			.willReturn(orderTables);

		//when, then
		assertThatThrownBy(()->tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class);
	}


	@DisplayName("테이블 그룹을 해제할 수 있다.")
	@Test
	void ungroup() {
		//given
		LocalDateTime createdDate = LocalDateTime.now();
		List<OrderTable> orderTables = Arrays.asList(
			new OrderTable(1L,1L,6, true),
			new OrderTable(2L,1L,3, true)
		);
		TableGroup tableGroup = new TableGroup(1L,createdDate, orderTables);


		given(orderTableDao.findAllByTableGroupId(any()))
			.willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(),anyList()))
			.willReturn(false);

		//when
		tableGroupService.ungroup(tableGroup.getId());

		//then
		List<OrderTable> ungroupedOrderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());
		ungroupedOrderTables.stream().forEach(table-> assertThat(table.getTableGroupId()).isNull());
	}

	@DisplayName("조리, 식사 상태의 주문 테이블이 존재하는 경우 테이블 그룹을 해제할 수 없다.")
	@Test
	void ungroup_exception() {
		//given
		LocalDateTime createdDate = LocalDateTime.now();
		List<OrderTable> orderTables = Arrays.asList(
			new OrderTable(1L,1L,6, true),
			new OrderTable(2L,1L,3, true)
		);
		TableGroup tableGroup = new TableGroup(1L,createdDate, orderTables);


		given(orderTableDao.findAllByTableGroupId(any()))
			.willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(),anyList()))
			.willReturn(true);

		//when, then
		assertThatThrownBy(()->tableGroupService.ungroup(tableGroup.getId()))
			.isInstanceOf(IllegalArgumentException.class);

	}

}
