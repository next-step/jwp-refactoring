package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.table.application.TableService;
import kitchenpos.table.ui.TableRestController;
import kitchenpos.domain.OrderTableRequest;

@WebMvcTest(TableRestController.class)
public class TableRestControllerTest extends WebMvcTestConfiguration {
	@MockBean
	private TableService tableService;

	@Test
	void createTest() throws Exception {
		given(tableService.create(any())).willReturn(new OrderTableRequest());

		mockMvc.perform(post("/api/tables")
			.content(objectMapper.writeValueAsString(new OrderTableRequest())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void listTest() throws Exception {
		given(tableService.list()).willReturn(Arrays.asList(new OrderTableRequest(1L, 1L, 10, false), new OrderTableRequest(2L, 2L, 4, true)));

		mockMvc.perform(get("/api/tables"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void changeEmptyTest() throws Exception {
		Long orderTableId = 1L;
		given(tableService.changeEmpty(any(), any())).willReturn(new OrderTableRequest());

		mockMvc.perform(put("/api/tables/" + orderTableId + "/empty")
			.param("orderTableId", orderTableId + "")
			.content(objectMapper.writeValueAsString(new OrderTableRequest()))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void changeNumberOfGuestTest() throws Exception {
		Long orderTableId = 1L;
		given(tableService.changeEmpty(any(), any())).willReturn(new OrderTableRequest());

		mockMvc.perform(put("/api/tables/" + orderTableId +"/number-of-guests")
			.param("orderTableId", orderTableId + "")
			.content(objectMapper.writeValueAsString(new OrderTableRequest()))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
