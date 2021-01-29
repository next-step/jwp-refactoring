package kitchenpos.ui;

import static java.util.function.Predicate.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupReponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.ui.TableGroupRestController;
import kitchenpos.table.ui.TableRestController;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql",
	"/db/migration/V3__remove_two_way.sql"})
class TableGroupRestControllerTest {

	@Autowired
	private TableRestController tableRestController;

	@Autowired
	private TableGroupRestController tableGroupRestController;

	private List<OrderTableRequest> 주문_테이블_요청;

	@BeforeEach
	void setUp() {
		주문_테이블_요청 = tableRestController.list()
			.getBody()
			.stream()
			.map(response -> new OrderTableRequest.Builder().id(response.getId()).build())
			.collect(Collectors.toList());
	}

	@Test
	void create() {
		// given
		TableGroupRequest 단체_지정_요청 = new TableGroupRequest(주문_테이블_요청);

		// when
		TableGroupReponse reponse = tableGroupRestController.create(단체_지정_요청).getBody();

		// then
		assertAll(
			() -> assertThat(reponse.getId()).isNotZero(),
			() -> assertThat(reponse.getOrderTables()).map(OrderTableResponse::isEmpty).allMatch(isEqual(false))
		);
	}

	@Test
	void ungroup() {
		// given
		TableGroupReponse created = tableGroupRestController.create(new TableGroupRequest(주문_테이블_요청)).getBody();

		// when
		tableGroupRestController.ungroup(created.getId());

		// then
		assertThat(tableRestController.list().getBody())
			.map(OrderTableResponse::getTableGroupId)
			.allMatch(Objects::isNull);
	}
}
