package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.TableRestController;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql",
	"/db/migration/V3__remove_two_way.sql"})
class TableRestControllerTest {

	@Autowired
	TableRestController tableRestController;

	@Test
	void create() {
		// given
		OrderTableRequest 주문_테이블_요청 = new OrderTableRequest.Builder()
				.empty(false)
				.numberOfGuests(4)
			.build();

		// when
		OrderTableResponse response = tableRestController.create(주문_테이블_요청).getBody();

		// then
		assertAll(
			() -> assertThat(response.getId()).isNotZero(),
			() -> assertThat(response.isEmpty()).isFalse(),
			() -> assertThat(response.getNumberOfGuests()).isEqualTo(4)
		);
	}

	@Test
	void list() {
		// given
		// @see V2__Insert_default_data.sql

		// when
		List<OrderTableResponse> listResponse = tableRestController.list().getBody();

		// then
		assertThat(listResponse)
			.hasSize(8)
			.allSatisfy(orderTableResponse -> assertThat(orderTableResponse.getNumberOfGuests()).isZero())
			.allSatisfy(orderTableResponse -> assertThat(orderTableResponse.isEmpty()).isTrue());
	}

	@Test
	void changeEmpty() {
		// given
		// @see V2__Insert_default_data.sql

		// given
		OrderTableRequest 임시_주문_테이블 = new OrderTableRequest.Builder().empty(false).build();
		OrderTableResponse response = tableRestController.changeEmpty(1L, 임시_주문_테이블).getBody();

		// then
		assertThat(response.isEmpty()).isFalse();
	}

	@Test
	void changeNumberOfGuests() {
		// given
		// @see V2__Insert_default_data.sql
		OrderTableRequest 임시_주문_테이블 = new OrderTableRequest.Builder().empty(false).build();
		tableRestController.changeEmpty(1L, 임시_주문_테이블);

		// when
		임시_주문_테이블 = new OrderTableRequest.Builder().numberOfGuests(0).build();
		OrderTableResponse response = tableRestController.changeNumberOfGuests(1L, 임시_주문_테이블).getBody();

		// then
		assertThat(response.getNumberOfGuests()).isZero();
	}
}
