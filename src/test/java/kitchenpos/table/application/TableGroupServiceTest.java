package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

	@InjectMocks
	private TableGroupService tableGroupService;

	@Mock
	private OrderTableRepository orderTableRepository;

	@Mock
	private TableGroupRepository tableGroupRepository;

	@Mock
	private OrderRepository orderRepository;

	private OrderTable 테이블_1번;
	private OrderTable 테이블_2번;
	private TableGroup 단체_테이블;

	@BeforeEach
	void setup() {

		테이블_1번 = OrderTable.of(1L, 2, false);
		테이블_2번 = OrderTable.of(2L, 4, false);
		단체_테이블 = TableGroup.of(1L, Arrays.asList(테이블_1번, 테이블_2번));

	}

	@DisplayName("단체 지정을 생성한다")
	@Test
	void createTest() {
		// given
		List<Long> tableIds = Stream.of(테이블_1번, 테이블_2번).map(OrderTable::getId)
			.collect(Collectors.toList());
		TableGroupRequest request = new TableGroupRequest(tableIds);

		given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(테이블_1번, 테이블_2번));
		given(tableGroupRepository.save(any())).willReturn(단체_테이블);

		// when
		TableGroupResponse response = tableGroupService.create(request);

		// then
		assertThat(response.getOrderTables().size()).isEqualTo(단체_테이블.getOrderTables().toList().size());
	}

	@DisplayName("생성 시, 주문 테이블 수가 2개 이상이어야 한다")
	@Test
	void createTest2() {
		// given
		List<Long> tableIds = Stream.of(테이블_1번).map(OrderTable::getId)
			.collect(Collectors.toList());
		TableGroupRequest request = new TableGroupRequest(tableIds);

		given(orderTableRepository.findAllById(any())).willReturn(Collections.singletonList(테이블_1번));

		// when, then
		assertThatThrownBy(() -> tableGroupService.create(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("생성 시, 주문 테이블들이 모두 존재해야 한다")
	@Test
	void createTest3() {
		// given
		List<Long> tableIds = Stream.of(테이블_1번).map(OrderTable::getId)
			.collect(Collectors.toList());
		TableGroupRequest request = new TableGroupRequest(tableIds);

		given(orderTableRepository.findAllById(any())).willReturn(new ArrayList<>());

		// when, then
		assertThatThrownBy(() -> tableGroupService.create(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("생성 시, 테이블 인원이 0인 주문테이블은 하나라도 있으면 안된다")
	@Test
	void createTest4() {
		// given
		List<Long> tableIds = Stream.of(테이블_1번).map(OrderTable::getId)
			.collect(Collectors.toList());
		TableGroupRequest request = new TableGroupRequest(tableIds);
		테이블_1번 = OrderTable.of(1L, 0, true);

		given(orderTableRepository.findAllById(any())).willReturn(new ArrayList<>());

		// when, then
		assertThatThrownBy(() -> tableGroupService.create(request))
			.isInstanceOf(AppException.class);
	}

	@DisplayName("단체 지정을 해제한다")
	@Test
	void ungroupTest() {
		// given
		given(tableGroupRepository.findById(any())).willReturn(Optional.of(단체_테이블));
		given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
			.willReturn(false);

		// when
		tableGroupService.ungroup(단체_테이블.getId());
	}

	@DisplayName("단체 지정을 해제 시, 조리 중이거나 식사 중인 테이블은 안된다")
	@Test
	void ungroupTest2() {
		// given
		given(tableGroupRepository.findById(any())).willReturn(Optional.of(단체_테이블));
		given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
			.willReturn(true);

		// when
		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(AppException.class);

	}

}
