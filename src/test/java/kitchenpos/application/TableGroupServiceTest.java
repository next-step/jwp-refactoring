package kitchenpos.application;

import static kitchenpos.generator.OrderTableGenerator.*;
import static kitchenpos.generator.TableGroupGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
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
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.ui.request.TableGroupRequest;
import kitchenpos.order.ui.request.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.order.ui.response.TableGroupResponse;

@DisplayName("단체 지정 서비스 테스트")
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

	private OrderTable 주문테이블1;
	private OrderTable 주문테이블2;
	private TableGroup 단체지정;

	@BeforeEach
	void setUp() {
		주문테이블1 = 주문테이블(5, true);
		주문테이블2 = 주문테이블(2, true);
		단체지정 = 단체_지정(Arrays.asList(주문테이블1, 주문테이블2));
	}

	@DisplayName("단체 지정을 할 수 있다.")
	@Test
	void createTableGroupTest() {
		// given
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
		given(tableGroupDao.save(any(TableGroup.class))).willReturn(단체지정);

		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

		// when
		TableGroupResponse response = tableGroupService.create(tableGroupRequest);

		// then
		verify(tableGroupDao, only()).save(any(TableGroup.class));
		assertAll(
			() -> assertThat(단체지정.orderTables().list()).hasSize(2),
			() -> assertThat(단체지정.id()).isEqualTo(response.getId())
		);
	}

	@DisplayName("단체 지정을 할 주문 테이블은 2개 이상이어야 한다.")
	@Test
	void createTableGroupWithOneOrderTableTest() {
		// given
		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Collections.singletonList(new OrderTableIdRequest(1L)));

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정을 할 주문 테이블은 모두 비어있어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		// given
		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));
		주문테이블1 = 주문테이블(2, false);

		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정을 할 주문 테이블은 이미 단체 지정이 되어 있지 않아야 한다.")
	@Test
	void createTableGroupWithAlreadyGroupedOrderTableTest() {
		// given
		주문테이블1 = 주문테이블(2, true);
		주문테이블2 = 주문테이블(5, true);

		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블을 단체 지정에서 제외할 수 있다.")
	@Test
	void ungroupTest() {
		// given
		List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);

		given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(주문_테이블_목록);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

		// when
		tableGroupService.ungroup(1L);

		// then
		verify(orderTableDao, times(2)).save(any(OrderTable.class));
		assertThat(주문_테이블_목록.stream()).allMatch(
			orderTable -> orderTable.tableGroup() == null && orderTable.isEmpty()
		);
	}

	@DisplayName("주문 상태가 식사중, 조리중이면 테이블을 단체 지정에서 제외할 수 없다.")
	@Test
	void ungroupWithNotCompletionOrderTableTest() {
		// given
		List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);

		given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(주문_테이블_목록);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.ungroup(1L));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}
}
