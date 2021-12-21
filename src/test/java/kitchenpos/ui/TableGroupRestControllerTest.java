package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@Transactional
@SpringBootTest
class TableGroupRestControllerTest {

	@Autowired
	private TableGroupRestController tableGroupRestController;

	@Test
	@DisplayName("테이블그룹 생성 테스트")
	public void createTableGroupTest() {
		//given
		OrderTable orderTable = new OrderTable(1L, null, 0, true);
		OrderTable otherOrderTable = new OrderTable(2L, null, 0, true);
		TableGroup tableGroup = new TableGroup(null, null, Lists.newArrayList(orderTable, otherOrderTable));
		//when
		ResponseEntity<TableGroup> responseEntity = tableGroupRestController.create(tableGroup);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/table-groups/2");
	}

	@Test
	@DisplayName("테이블그룹 해제 테스트")
	public void ungroupTableGroupTest() {
		//given
		//when
		ResponseEntity<Void> responseEntity = tableGroupRestController.ungroup(1L);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
