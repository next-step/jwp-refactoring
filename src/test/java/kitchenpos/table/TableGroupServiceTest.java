package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

	@Mock
	private TableGroupRepository tableGroupRepository;
	@Mock
	private ApplicationEventPublisher eventPublisher;
	@InjectMocks
	private TableGroupService tableGroupService;

	TableGroup 단체지정;
	OrderTable 주문테이블1번;
	OrderTable 주문테이블2번;

	@BeforeEach
	void setUp() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1));
		주문테이블2번 = TableServiceTest.주문테이블생성(2L, new NumberOfGuests(1));
		단체지정 = 단체지정생성(1L);
	}

	@DisplayName("단체지정 생성")
	@Test
	void 단체지정_생성() {
		TableGroupRequest 단체지정_생성_요청 = new TableGroupRequest(Arrays.asList(주문테이블1번.getId(), 주문테이블2번.getId()));
		given(tableGroupRepository.save(any())).willReturn(단체지정);

		TableGroupResponse created = tableGroupService.create(단체지정_생성_요청);

		단체지정_생성_확인(created, 단체지정);
	}



	@DisplayName("단체지정 해체")
	@Test
	void 단체지정_해체() {
		주문테이블1번 = TableServiceTest.주문테이블생성(1L, new NumberOfGuests(1), false);
		주문테이블2번 = TableServiceTest.주문테이블생성(2L, new NumberOfGuests(1), false);
		OrderLineItems 주문항목들 = mock(OrderLineItems.class);
		Order 주문 = new Order(주문테이블1번);
		Order 주문2 = new Order(주문테이블2번);
		주문.changeOrderStatus(OrderStatus.COMPLETION);
		주문2.changeOrderStatus(OrderStatus.COMPLETION);
		given(tableGroupRepository.findById(단체지정.getId())).willReturn(Optional.of(단체지정));

		tableGroupService.ungroup(단체지정.getId());

		단체지정_해체_확인(주문테이블1번);
		단체지정_해체_확인(주문테이블2번);
	}

	private void 단체지정_해체_확인(OrderTable ungroupOrderTable) {
		assertThat(ungroupOrderTable.getTableGroup()).isNull();
	}

	private void 단체지정_생성_확인(TableGroupResponse created, TableGroup expected) {
		assertThat(created.getId()).isEqualTo(expected.getId());
	}

	public static TableGroup 단체지정생성(Long id) {
		TableGroup tableGroup = new TableGroup(id);
		return tableGroup;
	}
}
