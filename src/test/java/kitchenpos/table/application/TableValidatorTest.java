package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.AppException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {

	@InjectMocks
	private TableValidator tableValidator;

	@Mock
	private OrderRepository orderRepository;

	private OrderTable 테이블_1번;
	private OrderTable 테이블_2번;

	@BeforeEach
	void setup() {

		테이블_1번 = OrderTable.of(1L, 2, false);
		테이블_2번 = OrderTable.of(2L, 4, false);

	}

	@DisplayName("단체 지정을 해제 시, 조리 중이거나 식사 중인 테이블은 안된다")
	@Test
	void ungroupTest2() {
		// given
		given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
			.willReturn(true);
		List<OrderTable> orderTables = Arrays.asList(테이블_1번, 테이블_2번);

		// when
		assertThatThrownBy(() -> tableValidator.validateUnGroup(orderTables))
			.isInstanceOf(AppException.class);
	}
}
