package kitchenpos.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableRestControllerTest extends MockMvcTest {

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void create() throws Exception {
		OrderTable orderTable = new OrderTable();

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/tables", orderTable))
				.andExpect(status().isCreated())
				.andReturn();
		OrderTable created = toObject(mvcResult, OrderTable.class);
		assertThat(created.getId()).isNotNull();
	}

	@DisplayName("주문 테이블을 조회한다.")
	@Test
	void list() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/tables")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<OrderTable> orderTables = toList(mvcResult, OrderTable.class);
		assertThat(orderTables).isNotEmpty();
	}

	@DisplayName("주문 테이블의 비어있는 상태를 변경한다.")
	@ParameterizedTest
	@CsvSource(value = {"5,true", "5,false"})
	void changeEmpty(long id, boolean isEmpty) throws Exception{
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(isEmpty);
		String uri = String.format("/api/tables/%d/empty", id);

		mockMvc.perform(putAsJson(uri, orderTable))
				.andExpect(status().isOk());
	}

	@DisplayName("주문 테이블의 인원수를 변경한다.")
	@ParameterizedTest
	@ValueSource(longs = {5})
	void changeNumberOfGuests(long id) throws Exception {
		changeEmpty(id, false);
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(50);
		String uri = String.format("/api/tables/%d/number-of-guests", id);

		mockMvc.perform(putAsJson(uri, orderTable))
				.andExpect(status().isOk());
	}
}
