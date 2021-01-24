package kitchenpos.ui;

import static java.util.function.Predicate.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.ui.TableGroupRestController;
import kitchenpos.table.ui.TableRestController;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql"})
class TableGroupRestControllerTest {

	@Autowired
	TableRestController tableRestController;

	@Autowired
	TableGroupRestController tableGroupRestController;

	@Test
	void create() {
		// given
		List<OrderTable> 주문_테이블_목록 = tableRestController.list().getBody();

		TableGroup 단체_지정 = new TableGroup();
		단체_지정.setOrderTables(주문_테이블_목록);

		// when
		TableGroup createdTableGroup = tableGroupRestController.create(단체_지정).getBody();

		// then
		assertAll(
			() -> assertThat(createdTableGroup.getId()).isNotZero(),
			() -> assertThat(createdTableGroup.getOrderTables()).map(OrderTable::isEmpty).allMatch(isEqual(false))
		);
	}

	@Test
	void ungroup() {
		// given
		List<OrderTable> 주문_테이블_목록 = tableRestController.list().getBody();
		TableGroup 단체_지정 = new TableGroup();
		단체_지정.setOrderTables(주문_테이블_목록);
		TableGroup createdTableGroup = tableGroupRestController.create(단체_지정).getBody();

		// when
		tableGroupRestController.ungroup(createdTableGroup.getId());

		// then
		assertThat(tableRestController.list().getBody()).map(OrderTable::getTableGroup).allMatch(Objects::isNull);
	}
}
