package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.event.OrderTableEventHandler;
import kitchenpos.table.event.OrderTableGroupEvent;
import kitchenpos.table.event.OrderTableUngroupEvent;
import kitchenpos.table.exception.TableGroupException;

@DisplayName("주문 테이블 이벤트 헨들러 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableEventHandlerTest {

	@Mock
	private TableRepository tableRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderTableEventHandler orderTableEventHandler;

	TableGroup 단체지정;
	OrderTable 주문테이블1번;
	OrderTable 주문테이블2번;

	@BeforeEach
	void setUp() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1));
		주문테이블2번 = TableServiceTest.주문테이블생성(2L, new NumberOfGuests(1));
		단체지정 = TableGroupServiceTest.단체지정생성(1L);
	}

	@DisplayName("주문테이블에 단체지정 매핑")
	@Test
	void 주문테이블에_단체지정_매핑() {
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()));
		given(tableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(주문테이블1번, 주문테이블2번));

		orderTableEventHandler.groupOrderTable(new OrderTableGroupEvent(단체지정, 단체지정_생성_요청.getOrderTableIds()));

		assertThat(주문테이블1번.getTableGroup()).isNotNull();
		assertThat(주문테이블2번.getTableGroup()).isNotNull();
	}

	@DisplayName("주문테이블에 단체지정 해체")
	@Test
	void 주문테이블에_단체지정_해체() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		주문테이블2번 = TableServiceTest.주문테이블생성(2L, new NumberOfGuests(1), false);
		Order 주문 = new Order(주문테이블1번);
		주문.changeOrderStatus(OrderStatus.COMPLETION);

		given(tableRepository.findAllByTableGroupId(1L)).willReturn(Arrays.asList(주문테이블1번, 주문테이블2번));
		given(orderRepository.findByOrderTableId(주문테이블1번.getId())).willReturn(주문);

		orderTableEventHandler.unGroupOrderTable(new OrderTableUngroupEvent(단체지정));

		assertThat(주문테이블1번.getTableGroup()).isNull();
		assertThat(주문테이블2번.getTableGroup()).isNull();
	}

	@DisplayName("주문테이블에 단체지정 매핑 - 주문 테이블이 빈테이블 경우 단체 지정에 등록될 수 없다.")
	@Test
	void 주문테이블에_단체지정_매핑_주문_테이블이_빈테이블일_경우_단체_지정에_등록될_수_없다() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()));
		assertThatThrownBy(() -> {
			orderTableEventHandler.groupOrderTable(new OrderTableGroupEvent(단체지정, 단체지정_생성_요청.getOrderTableIds()));
		}).isInstanceOf(TableGroupException.class);
	}

	@DisplayName("주문테이블에 단체지정 매핑 - 주문 테이블이 2개 미만인 경우 단체 지정에 등록될 수 없다.")
	@Test
	void 주문테이블에_단체지정_매핑_주문_테이블이_2개_미만인_경우_단체_지정에_등록될_수_없다() {
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId()));

		assertThatThrownBy(() -> {
			orderTableEventHandler.groupOrderTable(new OrderTableGroupEvent(단체지정, 단체지정_생성_요청.getOrderTableIds()));
		}).isInstanceOf(TableGroupException.class);
	}

	@DisplayName("주문테이블에 단체지정 매핑 - 주문 테이블이 존재하지않으면 단체 지정을 등록할 수 없다.")
	@Test
	void 주문테이블에_단체지정_매핑_주문_테이블이_존재하지않으면_단체_지정을_등록할_수_없다() {
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()));
		given(tableRepository.findAllByIdIn(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()))).willReturn(
			Arrays.asList(주문테이블1번));

		assertThatThrownBy(() -> {
			orderTableEventHandler.groupOrderTable(new OrderTableGroupEvent(단체지정, 단체지정_생성_요청.getOrderTableIds()));
		}).isInstanceOf(TableGroupException.class);
	}

	@DisplayName("주문테이블에 단체지정 해체 주문 테이블들이 조리중이거나 식사중일 경우 단체지정을 해체할 수 없다.")
	@Test
	void 주문테이블에_단체지정_해체_주문_테이블들이_조리중이거나_식사중일_경우_단체지정을_해체할_수_없다() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		OrderLineItems 주문항목들 = mock(OrderLineItems.class);
		Order 주문 = new Order(주문테이블1번);

		given(tableRepository.findAllByTableGroupId(단체지정.getId())).willReturn(Arrays.asList(주문테이블1번, 주문테이블2번));
		given(orderRepository.findByOrderTableId(주문테이블1번.getId())).willReturn(주문);
		given(orderRepository.findByOrderTableId(주문테이블2번.getId())).willReturn(주문);

		assertThatThrownBy(() -> {
			orderTableEventHandler.unGroupOrderTable(new OrderTableUngroupEvent(단체지정));
		}).isInstanceOf(TableGroupException.class);
	}
}
