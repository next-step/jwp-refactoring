package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

	@Test
	@DisplayName("테이블 그룹 생성 테스트")
	public void createTableGroupTest() {
		//when
		TableGroup tableGroup = new TableGroup();

		//then
		assertThat(tableGroup).isNotNull();
	}
}
