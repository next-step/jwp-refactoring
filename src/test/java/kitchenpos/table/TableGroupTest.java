package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.TableGroup;

@DisplayName("단체 지정 도메인 테스트")
public class TableGroupTest {

	@DisplayName("단체지정 생성")
	@Test
	void 단체지정_생성() {
		TableGroup tableGroup = new TableGroup(1L);
		assertThat(tableGroup).isNotNull();
		assertThat(tableGroup.getId()).isEqualTo(1L);
		assertThat(tableGroup.getCreatedDate()).isNotNull();
	}

}


