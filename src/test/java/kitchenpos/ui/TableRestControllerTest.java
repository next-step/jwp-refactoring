package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TableService tableService;

	@DisplayName("테이블 등록 요청")
	@Test
	public void create() throws Exception {
		OrderTable savedOrderTable = new OrderTable();
		savedOrderTable.setId(1L);
		savedOrderTable.setNumberOfGuests(0);
		savedOrderTable.setEmpty(true);

		when(tableService.create(any(OrderTable.class))).thenReturn(savedOrderTable);

		mockMvc.perform(
			post("/api/tables")
				.content("{\n"
					+ "  \"numberOfGuests\": 0,\n"
					+ "  \"empty\": true\n"
					+ "}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/tables/1"));
	}

	@DisplayName("테이블 목록 조 요청")
	@Test
	public void list() throws Exception {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(0);
		orderTable.setEmpty(true);

		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(orderTable);

		when(tableService.list()).thenReturn(orderTables);

		mockMvc.perform(get("/api/tables"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(orderTables)));
	}

	@DisplayName("빈 테이블 또는 주문 테이블로 변경")
	@Test
	public void changeEmpty() throws Exception {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setNumberOfGuests(0);
		orderTable.setEmpty(false);

		when(tableService.changeEmpty(anyLong(), any(OrderTable.class))).thenReturn(orderTable);

		mockMvc.perform(
			put("/api/tables/1/empty")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"empty\": false\n"
					+ "}")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(orderTable)));
	}

	@DisplayName("방문한 손님 수 변경")
	@Test
	public void changeNumberOfGuests() throws Exception {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setNumberOfGuests(4);
		orderTable.setEmpty(false);

		when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).thenReturn(orderTable);

		mockMvc.perform(
			put("/api/tables/1/number-of-guests")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\n"
					+ "  \"numberOfGuests\": 4\n"
					+ "}")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(orderTable)));
	}
}
