package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {

	@InjectMocks
	private TableGroupValidator tableGroupValidator;
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private OrderRepository orderRepository;

	private OrderTable 테이블_1번;
	private OrderTable 테이블_2번;
	private TableGroup 단체_테이블;

	@BeforeEach
	void setup() {

		테이블_1번 = OrderTable.of(1L, 2, false);
		테이블_2번 = OrderTable.of(2L, 4, false);
		단체_테이블 = TableGroup.of(1L);

	}

	@DisplayName("생성 시, 주문 테이블 수가 2개 이상이어야 한다")
	@Test
	void createTest2() {
		// given
		List<Long> tableIds = Stream.of(테이블_1번).map(OrderTable::getId)
			.collect(Collectors.toList());

		// when, then
		assertThatThrownBy(() -> tableGroupValidator.validateCreate(tableIds))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("생성 시, 주문 테이블들이 모두 등록되어 있어야 한다")
	@Test
	void createTest3() {
		// given
		List<OrderTable> orderTables = Arrays.asList(테이블_1번, 테이블_2번);
		List<Long> tableIds = orderTables.stream().map(OrderTable::getId)
			.collect(Collectors.toList());

		given(orderTableRepository.findAllById(tableIds)).willReturn(new ArrayList<>());

		// when, then
		assertThatThrownBy(() -> tableGroupValidator.validateCreate(tableIds))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("생성 시, 테이블 인원이 0인 주문테이블은 하나라도 있으면 안된다")
	@Test
	void createTest4() {
		// given
		테이블_1번 = OrderTable.of(1L, 0, true);
		List<OrderTable> orderTables = Arrays.asList(테이블_1번, 테이블_2번);
		List<Long> tableIds = orderTables.stream().map(OrderTable::getId)
			.collect(Collectors.toList());

		given(orderTableRepository.findAllById(any())).willReturn(orderTables);

		// when, then
		assertThatThrownBy(() -> tableGroupValidator.validateCreate(tableIds))
			.isInstanceOf(AppException.class);
	}

	@DisplayName("단체 지정을 해제 시, 조리 중이거나 식사 중인 테이블은 안된다")
	@Test
	void ungroupTest2() {
		// given
		given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
			.willReturn(true);
		List<OrderTable> orderTables = Arrays.asList(테이블_1번, 테이블_2번);

		// when
		assertThatThrownBy(() -> tableGroupValidator.validateUnGroup(orderTables))
			.isInstanceOf(AppException.class);

	}

}
