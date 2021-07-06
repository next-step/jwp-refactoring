package kitchenpos.application.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@Mock
	TableGroupDao tableGroupDao;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("단체 지정 생성.")
	@Test
	void 단체_지정_생성() {
		List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, null, 0, true),
			new OrderTable(2L, null, 0, true));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(
			Arrays.asList(new OrderTable(1L, null, 0, true), new OrderTable(2L, null, 0, true)));
		given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

		Assertions.assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);
	}

	@DisplayName("단체 지정 생성. > 주문 테이블 목록이 비어있거나 2보다 작으면 안됨.")
	@Test
	void 단체_지정_생성_주문_테이블_목록이_비어있거나_2보다_작으면_안됨() {
		TableGroup tableGroup1 = new TableGroup(1L, LocalDateTime.now(), null);

		List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, 1L, 3, false));
		TableGroup tableGroup2 = new TableGroup(1L, LocalDateTime.now(), orderTables);

		assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup1));
		assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup2));
	}

	@DisplayName("단체 지정 생성. > 주문 테이블 목록 갯수와 저장된 주문 테이블 목록 갯수가 다르면 안됨.")
	@Test
	void 단체_지정_생성_주문_테이블_목록_갯수와_저장된_주문_테이블_목록_갯수가_다르면_안됨() {
		List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, 1L, 3, false), new OrderTable(2L, 1L, 0, true));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(
			Arrays.asList(new OrderTable(1L, 1L, 3, false)));

		assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("단체 지정 생성. > 저장된 주문 테이블이 비어있거나 단체 지정이 차있으면 안됨.")
	@Test
	void 단체_지정_생성_저장된_주문_테이블이_비어있거나_단체_지정이_차있으면_안됨() {
		List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, 1L, 3, false), new OrderTable(2L, 1L, 0, true));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(
			Arrays.asList(new OrderTable(1L, 1L, 3, false), new OrderTable(2L, 1L, 0, true)));

		assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("단체 지정 해제. > 주문 테이블 목록이 요리 중이거나 식사 중이면 안됨")
	@Test
	void 단체_지정_해제_테이블_목록이_요리_중이거나_식사_중이면_안됨() {
		List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, 1L, 3, false), new OrderTable(2L, 1L, 0, true));
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

		given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
			.willReturn(orderTables);

		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
			orderTables.stream().map(OrderTable::getId).collect(Collectors.toList()),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
		).willReturn(true);

		assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
	}

}
