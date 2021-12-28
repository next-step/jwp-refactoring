package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@DisplayName("테이블 그룹 도메인 테스트")
public class TableGroupTest {

	private OrderTable 테이블_1번;
	private OrderTable 테이블_2번;
	private OrderTable 빈_테이블;
	private TableGroup 단체_테이블;

	@BeforeEach
	void setup() {

		테이블_1번 = OrderTable.of(1L, 2, false);
		테이블_2번 = OrderTable.of(2L, 4, false);
		빈_테이블 = OrderTable.of(3L, 0, true);
		단체_테이블 = TableGroup.of(1L, Arrays.asList(테이블_1번, 테이블_2번));

	}

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(TableGroup.of(1L, Arrays.asList(테이블_1번, 테이블_2번)))
			.isEqualTo(TableGroup.of(1L, Arrays.asList(테이블_1번, 테이블_2번)));
	}

	@DisplayName("주문 테이블이 최소 2개 이상이어야 한다")
	@Test
	void validateTest1() {
		// given
		List<OrderTable> tableList = Collections.singletonList(테이블_1번);

		// when, then
		assertThatThrownBy(() -> TableGroup.create(tableList))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("주문 테이블에 빈 테이블이 있으면 안된다")
	@Test
	void validateTest2() {
		// given
		List<OrderTable> tableList = Arrays.asList(테이블_1번, 빈_테이블);

		// then
		assertThatThrownBy(() -> TableGroup.create(tableList))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("단체 해지를 할 수 있다")
	@Test
	void unGroupTest() {
		// given

		// when
		단체_테이블.unGroup();

		// then
		assertAll(
			() -> assertThat(테이블_1번.getTableGroup()).isNull(),
			() -> assertThat(테이블_2번.getTableGroup()).isNull()
		);
	}
}
