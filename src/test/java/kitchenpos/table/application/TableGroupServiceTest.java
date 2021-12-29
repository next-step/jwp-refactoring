package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
import org.springframework.context.ApplicationEventPublisher;

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
	private TableGroupValidator tableGroupValidator;
	@Mock
	private ApplicationEventPublisher publisher;

	private OrderTable 테이블_1번;
	private OrderTable 테이블_2번;
	private TableGroup 단체_테이블;

	@BeforeEach
	void setup() {

		테이블_1번 = OrderTable.of(1L, 2, false);
		테이블_2번 = OrderTable.of(2L, 4, false);
		단체_테이블 = TableGroup.of(1L);

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
		assertThat(response.getOrderTables().size()).isEqualTo(2);
	}

	@DisplayName("단체 지정을 해제한다")
	@Test
	void ungroupTest() {
		// given
		List<OrderTable> tables = Arrays.asList(테이블_1번, 테이블_2번);
		given(tableGroupRepository.findById(any())).willReturn(Optional.of(단체_테이블));

		// when
		tableGroupService.ungroup(단체_테이블.getId());

		// then
		boolean isAllMatch = tables.stream().allMatch(orderTable -> Objects.isNull(orderTable.getTableGroup()));
		assertThat(isAllMatch).isTrue();
	}

}
