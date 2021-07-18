package kitchenpos.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
		OrderTableRequest orderTableRequest = new OrderTableRequest(2, true);
		OrderTableResponse orderTableResponse = new OrderTableResponse(1L, null, 2, true);

		given(tableService.create(orderTableRequest)).willReturn(orderTableResponse);

		mockMvc.perform(
				post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(orderTableRequest)))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", BASE_URL + "/1"));
	}

	@Test
	void listTest() throws Exception {
		List<OrderTableResponse> orderTableResponses = Lists.list(new OrderTableResponse(), new OrderTableResponse());
		given(tableService.list()).willReturn(orderTableResponses);

		mockMvc.perform(
				get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	@Test
	void changeEmptyTest() throws Exception {
		OrderTableResponse orderTableRequest = new OrderTableResponse(1L, 1L, 2, false);
		given(tableService.changeEmpty(1L, false)).willReturn(orderTableRequest);

		mockMvc.perform(
				put(BASE_URL + "/{orderTableId}/empty", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(false)))
				.andExpect(status().isOk());
	}

	@Test
	void changeNumberOfGuestsTest() throws Exception {
		OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 4, false);
		given(tableService.changeNumberOfGuests(1L, 4)).willReturn(orderTableResponse);

		mockMvc.perform(
				put(BASE_URL + "/{orderTableId}/number-of-guests", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(4)))
				.andExpect(status().isOk());
	}
}
