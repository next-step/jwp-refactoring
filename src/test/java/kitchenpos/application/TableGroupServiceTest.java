package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
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
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@Mock
	private TableGroupDao tableGroupDao;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("단체 지정")
	@Test
	void create_happyPath() {
		// given
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
		given(tableGroupDao.save(any(TableGroup.class))).willAnswer(invocation -> {
			TableGroup request = invocation.getArgument(0, TableGroup.class);
			request.setId(1L);
			return request;
		});

		// when
		OrderTableRequest 주문_테이블1_요청 = new OrderTableRequest.Builder().id(주문_테이블1.getId()).build();
		OrderTableRequest 주문_테이블2_요청 = new OrderTableRequest.Builder().id(주문_테이블2.getId()).build();
		TableGroupRequest 단체_지정_요청 = new TableGroupRequest(Arrays.asList(주문_테이블1_요청, 주문_테이블2_요청));
		TableGroupReponse 단체_지정_요청_응답 = tableGroupService.create(단체_지정_요청);

		// then
		assertThat(단체_지정_요청_응답.getId()).isEqualTo(1L);
		assertThat(단체_지정_요청_응답.getOrderTables())
			.map(OrderTableResponse::getId)
			.contains(주문_테이블1.getId(), 주문_테이블2.getId());
		assertThat(단체_지정_요청_응답.getOrderTables())
			.map(OrderTableResponse::isEmpty)
			.containsExactly(false, false);
	}

	@DisplayName("단체 지정 : 문 테이블들이 이미 단체 지정 되어있음")
	@Test
	void create_exceptionCase1() {
		// given
		TableGroup 이미_단체_지정 = new TableGroup.Builder().id(-1L).build();
		OrderTable 주문_테이블9 = new OrderTable.Builder().id(9L).tableGroup(이미_단체_지정).empty(true).build();
		OrderTable 주문_테이블10 = new OrderTable.Builder().id(10L).tableGroup(이미_단체_지정).empty(true).build();
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블9, 주문_테이블10));

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
		OrderTable 주문_테이블10 = new OrderTable.Builder().id(10L).empty(false).build();
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블9, 주문_테이블10).build();
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블9, 주문_테이블10));

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
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블1, 주문_테이블2).build();
		주문_테이블1.setTableGroup(단체_지정);
		주문_테이블2.setTableGroup(단체_지정);
		given(orderTableDao.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			anyList(),
			eq(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
		).willReturn(false);

		// when
		tableGroupService.ungroup(단체_지정.getId());

		// then
		assertThat(주문_테이블1.getTableGroup()).isNull();
		assertThat(주문_테이블2.getTableGroup()).isNull();
	}

	@DisplayName("단체 해제 : 단체 지정되어 있던 주문 테이블의 주문 상태가 계산 완료가 아닌 것이 존재함")
	@Test
	void ungroup_exceptionCase() {
		// given
		TableGroup 단체_지정 = new TableGroup.Builder().orderTables(주문_테이블1, 주문_테이블2).build();
		주문_테이블1.setTableGroup(단체_지정);
		주문_테이블2.setTableGroup(단체_지정);
		given(orderTableDao.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			anyList(),
			eq(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
		).willReturn(true);

		// when & then
		assertThatThrownBy(() -> tableGroupService.ungroup(단체_지정.getId())).isInstanceOf(IllegalArgumentException.class);
	}
}
