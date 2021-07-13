package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.TableGroupException;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private TableGroupRepository tableGroupRepository;
	@Mock
	private TableRepository tableRepository;
	@InjectMocks
	private TableGroupService tableGroupService;

	TableGroup 단체지정;
	OrderTable 주문테이블1번;
	OrderTable 주문테이블2번;

	@BeforeEach
	void setUp() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1));
		주문테이블2번 = TableServiceTest.주문테이블생성(2L, new NumberOfGuests(1));
		단체지정 = 단체지정생성(1L, Arrays.asList(주문테이블1번, 주문테이블2번));
	}

	@DisplayName("단체지정 생성")
	@Test
	void 단체지정_생성() {
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()));
		given(tableRepository.findAllByIdIn(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()))).willReturn(
			Arrays.asList(주문테이블1번, 주문테이블2번));
		given(tableGroupRepository.save(any())).willReturn(단체지정);

		TableGroupResponse created = tableGroupService.create(단체지정_생성_요청);

		단체지정_생성_확인(created, 단체지정);
	}

	@DisplayName("단체지정 생성 - 주문 테이블이 빈테이블 경우 단체 지정에 등록될 수 없다.")
	@Test
	void 단체지정_생성_주문_테이블이_빈테이블일_경우_단체_지정에_등록될_수_없다() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()));
		assertThatThrownBy(() -> {
			tableGroupService.create(단체지정_생성_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체지정 생성 - 주문 테이블이 2개 미만인 경우 단체 지정에 등록될 수 없다.")
	@Test
	void 단체지정_생성_주문_테이블이_2개_미만인_경우_단체_지정에_등록될_수_없다() {
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId()));

		assertThatThrownBy(() -> {
			tableGroupService.create(단체지정_생성_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체지정 생성 - 주문 테이블이 존재하지않으면 단체 지정을 등록할 수 없다.")
	@Test
	void 단체지정_생성_주문_테이블이_존재하지않으면_단체_지정을_등록할_수_없다() {
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()));
		given(tableRepository.findAllByIdIn(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()))).willReturn(
			Arrays.asList(주문테이블1번));

		assertThatThrownBy(() -> {
			tableGroupService.create(단체지정_생성_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체지정 해체")
	@Test
	void 단체지정_해체() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		주문테이블2번 = TableServiceTest.주문테이블생성(2L, new NumberOfGuests(1), false);
		OrderLineItems 주문항목들 = mock(OrderLineItems.class);
		Order 주문 = new Order(주문테이블1번, 주문항목들);
		Order 주문2 = new Order(주문테이블2번, 주문항목들);
		주문.changeOrderStatus(OrderStatus.COMPLETION);
		주문2.changeOrderStatus(OrderStatus.COMPLETION);
		given(tableGroupRepository.findById(단체지정.getId())).willReturn(Optional.of(단체지정));
		given(orderRepository.findByOrderTableId(주문테이블1번.getId())).willReturn(주문);
		given(orderRepository.findByOrderTableId(주문테이블2번.getId())).willReturn(주문2);

		tableGroupService.ungroup(단체지정.getId());

		단체지정_해체_확인(주문테이블1번);
		단체지정_해체_확인(주문테이블2번);
	}

	@DisplayName("단체지정 해체 - 주문 테이블들이 조리중이거나 식사중일 경우 단체지정을 해체할 수 없다.")
	@Test
	void 단체지정_해체_주문_테이블들이_조리중이거나_식사중일_경우_단체지정을_해체할_수_없다() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		OrderLineItems 주문항목들 = mock(OrderLineItems.class);
		Order 주문 = new Order(주문테이블1번, 주문항목들);

		given(tableGroupRepository.findById(단체지정.getId())).willReturn(Optional.of(단체지정));
		given(orderRepository.findByOrderTableId(주문테이블1번.getId())).willReturn(주문);

		assertThatThrownBy(() -> {
			tableGroupService.ungroup(단체지정.getId());
		}).isInstanceOf(TableGroupException.class);
	}

	private void 단체지정_해체_확인(OrderTable ungroupOrderTable) {
		assertThat(ungroupOrderTable.getTableGroup()).isNull();
	}

	private void 단체지정_생성_확인(TableGroupResponse created, TableGroup expected) {
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.getOrderTableResponses()).isNotNull();
		assertThat(created.getOrderTableResponses()).isNotEmpty();
		OrderTableResponse createdOrderTable = created.getOrderTableResponses().stream().findAny().get();
		assertThat(createdOrderTable.isEmpty()).isTrue();
	}

	public static TableGroup 단체지정생성(Long id, List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup(id, orderTables);
		return tableGroup;
	}
}
