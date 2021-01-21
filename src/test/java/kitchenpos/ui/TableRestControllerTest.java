package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;

@DisplayName("테이블 Controller 테스트")
@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private TableService tableService;

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void createOrderTableTest() throws Exception {
		// given
		OrderTable orderTable = OrderTable.of(1L, null, 0, false);
		given(tableService.create(any())).willReturn(orderTable);

		// when
		final ResultActions resultActions = mvc.perform(post("/api/tables")
			.content(mapper.writeValueAsString(orderTable))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl("/api/tables" + "/" + orderTable.getId()))
			.andExpect(jsonPath("$.id").value(orderTable.getId()))
			.andDo(log());
	}

	@DisplayName("주문 테이블을 조회한다.")
	@Test
	void selectOrderTableListTest() throws Exception {
		// given
		OrderTable table1 = OrderTable.of(1L, 1L, 3, true);
		OrderTable table2 = OrderTable.of(2L, null, 0, false);
		given(tableService.list()).willReturn(Arrays.asList(table1, table2));

		// when
		final ResultActions resultActions = mvc.perform(get("/api/tables")
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.[0].id").value(1L))
			.andExpect(jsonPath("$.[1].id").value(2L));
	}

	@DisplayName("비어있는 테이블로 설정한다.")
	@Test
	void setEmptyTableTest() throws Exception {
		// given
		OrderTable expectedTable = OrderTable.of(1L, 1L, 3, false);
		given(tableService.changeEmpty(any(), any())).willReturn(expectedTable);

		// when
		final ResultActions resultActions = mvc.perform(put("/api/tables/{orderTableId}/empty", expectedTable.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(expectedTable)))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.empty").value(false));
	}

	@DisplayName("테이블 손님 수를 변경한다.")
	@Test
	void changeNumberOfGuestsTest() throws Exception {
		// given
		OrderTable expectedTable = OrderTable.of(1L, 1L, 3, true);
		given(tableService.changeNumberOfGuests(any(), any())).willReturn(expectedTable);

		// when
		final ResultActions resultActions = mvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(expectedTable)))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.numberOfGuests").value(expectedTable.getNumberOfGuests()));
	}
}
