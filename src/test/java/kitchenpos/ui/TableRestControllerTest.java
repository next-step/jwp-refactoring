package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("테이블 컨트롤러 테스트")
@WebMvcTest(TableRestController.class)
public class TableRestControllerTest {
	private static final String BASE_URL = "/api/tables";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private TableRestController tableRestController;

	@MockBean
	private TableService tableService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(print())
				.build();
	}

	@Test
	void createTest() throws Exception {
		OrderTable orderTable = new OrderTable(1L, null, 2, true);
		given(tableService.create(any(OrderTable.class))).willReturn(orderTable);

		mockMvc.perform(
				post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderTable)))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", BASE_URL + "/1"));
	}

	@Test
	void listTest() throws Exception {
		List<OrderTable> orderTables = Lists.list(new OrderTable(), new OrderTable());
		given(tableService.list()).willReturn(orderTables);

		mockMvc.perform(
				get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	@Test
	void changeEmptyTest() throws Exception {
		OrderTable orderTable = new OrderTable(1L, 1L, 2, true);
		given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(orderTable);

		mockMvc.perform(
				put(BASE_URL + "/{orderTableId}/empty", orderTable.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderTable)))
				.andExpect(status().isOk());
	}

	@Test
	void changeNumberOfGuestsTest() throws Exception {
		OrderTable orderTable = new OrderTable(1L, 1L, 2, true);
		given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(orderTable);

		mockMvc.perform(
				put(BASE_URL + "/{orderTableId}/number-of-guests", orderTable.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderTable)))
				.andExpect(status().isOk());
	}
}
