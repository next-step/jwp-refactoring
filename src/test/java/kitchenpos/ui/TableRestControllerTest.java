package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.OrderTable;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql"})
class TableRestControllerTest {

	@Autowired
	TableRestController tableRestController;

	@Test
	void create() {
		// given
		OrderTable 주문_테이블 = new OrderTable();
		주문_테이블.setEmpty(false);
		주문_테이블.setNumberOfGuests(4);

		// when
		OrderTable createdOrderTable = tableRestController.create(주문_테이블).getBody();

		// then
		assertAll(
			() -> assertThat(createdOrderTable.getId()).isNotZero(),
			() -> assertThat(createdOrderTable.isEmpty()).isFalse(),
			() -> assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(4)
		);
	}

	@Test
	void list() {
		// given
		// @see V2__Insert_default_data.sql

		// when
		List<OrderTable> orderTableList = tableRestController.list().getBody();

		// then
		assertThat(orderTableList)
			.hasSize(8)
			.allSatisfy(orderTable -> assertThat(orderTable.getNumberOfGuests()).isZero())
			.allSatisfy(orderTable -> assertThat(orderTable.isEmpty()).isTrue());
	}

	@Test
	void changeEmpty() {
		// given
		// @see V2__Insert_default_data.sql

		// when
		OrderTable 임시_주문_테이블 = new OrderTable();
		임시_주문_테이블.setId(1L);
		임시_주문_테이블.setEmpty(false);
		OrderTable updatedTable = tableRestController.changeEmpty(1L, 임시_주문_테이블).getBody();

		// then
		assertThat(updatedTable.isEmpty()).isFalse();
	}

	@Test
	void changeNumberOfGuests() {
		// given
		// @see V2__Insert_default_data.sql
		OrderTable 임시_주문_테이블1 = new OrderTable();
		임시_주문_테이블1.setId(2L);
		임시_주문_테이블1.setEmpty(false);
		tableRestController.changeEmpty(1L, 임시_주문_테이블1);

		// when
		OrderTable 임시_주문_테이블2 = new OrderTable();
		임시_주문_테이블2.setId(2L);
		임시_주문_테이블2.setNumberOfGuests(0);
		OrderTable updatedTable = tableRestController.changeNumberOfGuests(1L, 임시_주문_테이블2).getBody();

		// then
		assertThat(updatedTable.getNumberOfGuests()).isZero();
	}
}
