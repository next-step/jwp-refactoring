package kitchenpos.table.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.common.Empty;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

@DisplayName("주문테이블 목록 관련 단위테스트")
public class OrderTablesTest {
	private OrderTable 빈_테이블_A;
	private OrderTable 빈_테이블_B;
	private OrderTable 채워진_테이블;

	private OrderTables 단체_지정_대상_테이블_목록;

	@BeforeEach
	void setUp() {
		채워진_테이블 = OrderTable.of(2, false);
		빈_테이블_A = OrderTable.of(2, true);
		빈_테이블_B = OrderTable.of(2, true);
		ReflectionTestUtils.setField(채워진_테이블, "id", 1L);
		ReflectionTestUtils.setField(빈_테이블_A, "id", 2L);
		ReflectionTestUtils.setField(빈_테이블_B, "id", 3L);
	}

	@DisplayName("이미 단체지정된 테이블의 비움상태를 변경할때 예외가 발생한다.")
	@Test
	void updateEmptyStatus_when_grouped_exception() {
		// given
		List<OrderTable> orderTables = Arrays.asList(빈_테이블_A, 빈_테이블_B);
		orderTables.stream().forEach(it -> it.group(1L));
		// when - then
		assertThatThrownBy(() -> 빈_테이블_A.updateEmptyStatus(Empty.of(false)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.CANNOT_CHANGE_EMPTINESS_WHEN_TABLE_GROUPED);
	}

	@DisplayName("이미 단체지정된 테이블을 다시 단체지정 대상으로 정할때 에러가 발생한다.")
	@Test
	void create_when_grouped_exception() {
		// given
		List<OrderTable> orderTables = Arrays.asList(빈_테이블_A, 빈_테이블_B);
		orderTables.stream().forEach(it -> it.group(1L));

		// when - then
		assertThatThrownBy(() -> OrderTables.of(orderTables).group(2L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_ALREADY_GROUPED);
	}

	@DisplayName("채워진 테이블을 단체지정하려 할때 예외가 발생한다.")
	@Test
	void create_when_empty_exception() {
		단체_지정_대상_테이블_목록 = OrderTables.of(Arrays.asList(채워진_테이블, 빈_테이블_B));
		// when - then
		assertThatThrownBy(() -> 단체_지정_대상_테이블_목록.group(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_IS_NOT_ALL_EMPTY);
	}

	@DisplayName("최소갯수 미만의 테이블을 단체지정 하려 할때 예외가 발생한다.")
	@Test
	void create_when_size_below_two_exception() {
		단체_지정_대상_테이블_목록 = OrderTables.of(Arrays.asList(빈_테이블_B));
		// when - then
		assertThatThrownBy(() -> 단체_지정_대상_테이블_목록.group(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_SIZE_SIZE_IS_TOO_SMALL);
	}
}
