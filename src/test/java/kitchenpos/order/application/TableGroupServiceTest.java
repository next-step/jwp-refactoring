package kitchenpos.order.application;

import static kitchenpos.TestInstances.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.TestInstances;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
	@InjectMocks
	private TableGroupService tableGroupService;
	@Mock
	private TableService tableService;
	@Mock
	private OrderService orderService;
	@Mock
	private TableGroupRepository tableGroupRepository;

	@BeforeEach
	void setUp() {
		TestInstances.init();
	}

	@DisplayName("단체 지정")
	@Test
	void create() {
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
			new OrderTableRequest(테이블1.getId()),
			new OrderTableRequest(테이블2.getId())
		));

		when(tableService.findAllOrderTablesByIds(anyList())).thenReturn(Arrays.asList(테이블1, 테이블2));
		when(tableGroupRepository.save(any(TableGroup.class))).thenAnswer(invocation -> {
			TableGroup tableGroup = invocation.getArgument(0, TableGroup.class);
			ReflectionTestUtils.setField(tableGroup, "id", 1L);
			return tableGroup;
		});

		TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

		tableGroupResponse.getOrderTables()
			.forEach(orderTableResponse -> assertThat(orderTableResponse.getTableGroupId()).isEqualTo(1L));
		assertThat(tableGroupResponse.getOrderTables().get(0).getTableGroupId()).isEqualTo(1L);
		assertThat(tableGroupResponse.getOrderTables().get(0).getTableGroupId()).isEqualTo(1L);
	}

	@DisplayName("2개 미만의 테이블 단체 지정시 실패")
	@Test
	void createWhenLessThanTwo() {
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
			new OrderTableRequest(테이블1.getId())
		));

		when(tableService.findAllOrderTablesByIds(anyList())).thenReturn(Arrays.asList(테이블1));
		when(tableGroupRepository.save(any(TableGroup.class))).thenAnswer(invocation -> {
			TableGroup tableGroup = invocation.getArgument(0, TableGroup.class);
			ReflectionTestUtils.setField(tableGroup, "id", 1L);
			return tableGroup;
		});

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroupRequest));
	}

	@DisplayName("이미 단체 지정이 된 테이블 지정시 실패")
	@Test
	void createWhenAlreadyGrouping() {
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
			new OrderTableRequest(테이블1.getId()),
			new OrderTableRequest(테이블2.getId())
		));
		테이블1.setTableGroupId(1L);

		when(tableService.findAllOrderTablesByIds(anyList())).thenReturn(Arrays.asList(테이블1, 테이블2));
		when(tableGroupRepository.save(any(TableGroup.class))).thenAnswer(invocation -> {
			TableGroup tableGroup = invocation.getArgument(0, TableGroup.class);
			ReflectionTestUtils.setField(tableGroup, "id", 1L);
			return tableGroup;
		});

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroupRequest));
	}

	@DisplayName("비어 있지 않은 테이블 단체 지정시 실패")
	@Test
	void createWhenOrderTables() {
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
			new OrderTableRequest(테이블1.getId()),
			new OrderTableRequest(테이블2.getId())
		));
		테이블1.changeEmpty(false);

		when(tableService.findAllOrderTablesByIds(anyList())).thenReturn(Arrays.asList(테이블1, 테이블2));
		when(tableGroupRepository.save(any(TableGroup.class))).thenAnswer(invocation -> {
			TableGroup tableGroup = invocation.getArgument(0, TableGroup.class);
			ReflectionTestUtils.setField(tableGroup, "id", 1L);
			return tableGroup;
		});

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroupRequest));
	}

	@DisplayName("단체 지정 해제")
	@Test
	void ungroup() {
		TableGroup tableGroup = new TableGroup();
		tableGroup.group(Arrays.asList(테이블1, 테이블2));

		when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(tableGroup));
		when(orderService.findAllOrderByOrderTableIds(anyList())).thenReturn(Collections.emptyList());

		tableGroupService.ungroup(1L);

		verify(tableGroupRepository, times(1)).deleteById(anyLong());
		assertThat(테이블1.getTableGroupId()).isNull();
		assertThat(테이블2.getTableGroupId()).isNull();
	}

	@DisplayName("완료되지 않은 테이블이 있을시 해제 실패")
	@Test
	void ungroupWhenNotCompletion() {
		TableGroup tableGroup = new TableGroup();
		tableGroup.group(Arrays.asList(테이블1, 테이블2));

		when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(tableGroup));
		when(orderService.findAllOrderByOrderTableIds(anyList())).thenReturn(
			Arrays.asList(Order.builder()
				.orderTable(테이블1)
				.orderLineItems(Arrays.asList(OrderLineItem.builder().menu(후라이드치킨메뉴).build()))
				.orderStatus(OrderStatus.MEAL.name())
				.build()));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.ungroup(1L));
	}
}
