package kitchenpos.ordertable.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 그룹 테스트")
public class TableGroupTest {

	@DisplayName("테이블 그룹 생성")
	@Test
	void create() {
		TableGroup tableGroup = new TableGroup();

		assertThat(tableGroup).isNotNull();
	}

}
