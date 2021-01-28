package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupReponse;
import kitchenpos.table.dto.TableGroupRequest;

@DisplayName("단체 지정 BO 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	private TableService tableService;
	@Mock
	private TableGroupDao tableGroupDao;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("단체 지정")
	@Test
	void create_happyPath() {
		// given
		OrderTable 새_주문_테이블1 = new OrderTable.Builder().id(9L).empty(true).build();
		OrderTable 새_주문_테이블2 = new OrderTable.Builder().id(10L).empty(true).build();
		given(tableService.findById(새_주문_테이블1.getId())).willReturn(새_주문_테이블1);
		given(tableService.findById(새_주문_테이블2.getId())).willReturn(새_주문_테이블2);
		given(tableGroupDao.save(any(TableGroup.class))).willAnswer(invocation -> {
			TableGroup mock = spy(invocation.getArgument(0, TableGroup.class));
			when(mock.getId()).thenReturn(1L);
			return mock;
		});

		// when
		OrderTableRequest 주문_테이블1_요청 = new OrderTableRequest.Builder().id(새_주문_테이블1.getId()).build();
		OrderTableRequest 주문_테이블2_요청 = new OrderTableRequest.Builder().id(새_주문_테이블2.getId()).build();
		TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(주문_테이블1_요청, 주문_테이블2_요청));
		TableGroupReponse 단체_지정_요청_응답 = tableGroupService.create(단체_지정_요청);

		// then
		assertThat(단체_지정_요청_응답.getId()).isEqualTo(1L);
		assertThat(단체_지정_요청_응답.getOrderTables())
			.map(OrderTableResponse::getId)
			.contains(새_주문_테이블1.getId(), 새_주문_테이블2.getId());
		assertThat(단체_지정_요청_응답.getOrderTables())
			.map(OrderTableResponse::isEmpty)
			.containsExactly(false, false);
	}

	@DisplayName("단체 지정 : 문 테이블들이 이미 단체 지정 되어있음")
	@Test
	void create_exceptionCase1() {
		// given
		OrderTable 주문_테이블9 = new OrderTable.Builder().id(9L).empty(true).build();
		OrderTable 주문_테이블10 = new OrderTable.Builder().id(10L).empty(true).build();
		TableGroup 이미_단체_지정 = new TableGroup.Builder().id(-1L).orderTables(주문_테이블9, 주문_테이블10).build();
		given(tableService.findById(주문_테이블9.getId())).willReturn(주문_테이블9);
		given(tableService.findById(주문_테이블10.getId())).willReturn(주문_테이블10);

		// when & then
		OrderTableRequest 주문_테이블9_요청 = new OrderTableRequest.Builder().id(주문_테이블9.getId()).build();
		OrderTableRequest 주문_테이블10_요청 = new OrderTableRequest.Builder().id(주문_테이블10.getId()).build();
		TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(주문_테이블9_요청, 주문_테이블10_요청));

		assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정 : 주문 테이블들은 빈 테이블이 아님")
	@Test
	void create_exceptionCase2() {
		// given
		OrderTable 주문_테이블9 = new OrderTable.Builder().id(9L).empty(false).build();
		OrderTable 주문_테이블10 = new OrderTable.Builder().id(10L).empty(true).build();
		given(tableService.findById(주문_테이블9.getId())).willReturn(주문_테이블9);
		given(tableService.findById(주문_테이블10.getId())).willReturn(주문_테이블10);

		// when & then
		OrderTableRequest 주문_테이블9_요청 = new OrderTableRequest.Builder().id(주문_테이블9.getId()).build();
		OrderTableRequest 주문_테이블10_요청 = new OrderTableRequest.Builder().id(주문_테이블10.getId()).build();
		TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(주문_테이블9_요청, 주문_테이블10_요청));
		assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 해제")
	@Test
	void ungroup() {
		// given
		OrderTable 주문_테이블9 = new OrderTable.Builder().id(9L).empty(true).build();
		OrderTable 주문_테이블10 = new OrderTable.Builder().id(10L).empty(true).build();
		TableGroup 단체_지정 = new TableGroup.Builder().id(-1L).orderTables(주문_테이블9, 주문_테이블10).build();
		given(tableService.findAllByTableGroupId(단체_지정.getId())).willAnswer(invocation -> {
			Set<OrderTable> orderTableSet = new HashSet<>();
			orderTableSet.add(주문_테이블9);
			orderTableSet.add(주문_테이블10);
			return orderTableSet;
		});

		// when
		tableGroupService.ungroup(단체_지정.getId());

		// then
		assertThat(주문_테이블9.getTableGroup()).isNull();
		assertThat(주문_테이블10.getTableGroup()).isNull();
	}

	@DisplayName("단체 해제 : 단체 지정되어 있던 주문 테이블의 주문 상태가 계산 완료가 아닌 것이 존재함")
	@Test
	void ungroup_exceptionCase() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴2).quantity(2L).build();
		OrderTable 새_주문_테이블1 = new OrderTable.Builder().id(-1L).empty(false)
			.orders(
				new Order.Builder()
					.orderStatus(OrderStatus.COOKING)
					.orderLineItems(주문_항목1)
					.orderTableId(-1L)
					.build(),
				new Order.Builder()
					.orderStatus(OrderStatus.MEAL)
					.orderLineItems(주문_항목2)
					.orderTableId(-1L)
					.build()
			)
			.build();
		OrderTable 새_주문_테이블2 = new OrderTable.Builder().id(-2L).empty(false)
			.orders(
				new Order.Builder()
					.orderStatus(OrderStatus.COOKING)
					.orderLineItems(주문_항목1)
					.orderTableId(-2L)
					.build(),
				new Order.Builder()
					.orderStatus(OrderStatus.MEAL)
					.orderLineItems(주문_항목2)
					.orderTableId(-2L)
					.build()
			)
			.build();
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블1, 주문_테이블2).build();
		given(tableService.findAllByTableGroupId(단체_지정.getId())).willAnswer(invocation -> {
			Set<OrderTable> orderTableSet = new HashSet<>();
			orderTableSet.add(새_주문_테이블1);
			orderTableSet.add(새_주문_테이블2);
			return orderTableSet;
		});

		// when & then
		assertThatThrownBy(() -> tableGroupService.ungroup(단체_지정.getId())).isInstanceOf(IllegalArgumentException.class);
	}
}
