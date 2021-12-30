package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
		단체_테이블 = TableGroup.of(1L);

	}

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(TableGroup.of(1L))
			.isEqualTo(TableGroup.of(1L));
	}

}
