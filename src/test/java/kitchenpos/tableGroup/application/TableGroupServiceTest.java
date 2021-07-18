package kitchenpos.tableGroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.application.TableGroupService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
	private OrderTableRequest orderTableWithFivePeopleRequest;
	private OrderTableRequest orderTableWithTenPeopleRequest;
	private OrderTable orderTableWithFivePeople;
	private OrderTable orderTableWithTenPeople;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderTableRepository orderTableRepository;

	@Mock
	private TableGroupRepository tableGroupRepository;

	@InjectMocks
	private TableGroupService tableGroupService;

	@BeforeEach
	void setUp() {
		orderTableWithFivePeopleRequest = new OrderTableRequest(1L, null, 5, true);
		orderTableWithTenPeopleRequest = new OrderTableRequest(2L, null, 10, true);
		orderTableWithFivePeople = new OrderTable(new NumberOfGuests(5), true);
		orderTableWithTenPeople = new OrderTable(new NumberOfGuests(10), true);

	}

	@DisplayName("단체 지정을 등록한다.")
	@Test
	void createTestInHappyCase() {
		// given
		when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		when(tableGroupRepository.save(any())).thenReturn(new TableGroup(LocalDateTime.now(), Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople)));
		// when
		TableGroupResponse tableGroup = tableGroupService.create(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest, orderTableWithTenPeopleRequest)));
		// then
		assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
	}

	@DisplayName("단체 지정을 할 때에는 요청 주문 테이블이 2보다 커야 한다.")
	@Test
	void createTestWithOneOrderTable() {
		// given
		lenient().when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		lenient().when(tableGroupRepository.save(any())).thenReturn(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest, orderTableWithTenPeopleRequest)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest))));
	}

	@DisplayName("주문 테이블은 먼저 등록되어 있어야 한다.")
	@Test
	void createTestWithNotExistsOrderTable() {
		// given
		lenient().when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(null);
		lenient().when(tableGroupRepository.save(any())).thenReturn(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest, orderTableWithTenPeopleRequest)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest))));
	}

	@DisplayName("기 단체 지정된 주문 테이블은 새롭게 단체 지정할 수 없다.")
	@Test
	void createTestWithAlreadyTableGroupedOrderTable() {
		// given
		lenient().when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		lenient().when(tableGroupRepository.save(any())).thenReturn(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest, orderTableWithTenPeopleRequest)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest))));
	}

	@DisplayName("요청 주문 테이블은 비어 있어야만 한다.")
	@Test
	void createTestWithEmptyOrderTable() {
		// given
		lenient().when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(new OrderTable(new NumberOfGuests(5), false), new OrderTable(new NumberOfGuests(10), true)));
		lenient().when(tableGroupRepository.save(any())).thenReturn(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest, orderTableWithTenPeopleRequest)));
		// when, then
		assertThatThrownBy(() -> tableGroupService.create(new kitchenpos.tableGroup.dto.TableGroupRequest(LocalDateTime.now(), Arrays.asList(
			orderTableWithFivePeopleRequest, orderTableWithTenPeopleRequest))));
	}

	@DisplayName("단체 지정 해제한다.")
	@Test
	void ungroupTestInHappyCase() {
		// given
		when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
		// when
		tableGroupService.ungroup(1L);
		// then
	}

	@DisplayName("COOKING, MEAL 상태인 주문 테이블이 존재할 경우 단체 지정을 해제할 수 없다.")
	@Test
	void ungroupTestWithCookingOrMealStatus() {
		// given
		when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTableWithFivePeople, orderTableWithTenPeople));
		when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
		// when, then
		assertThatThrownBy(() -> tableGroupService.ungroup(1L));
	}
}
