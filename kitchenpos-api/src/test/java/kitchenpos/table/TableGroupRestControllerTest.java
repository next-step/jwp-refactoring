package kitchenpos.table;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TableGroupService tableGroupService;

	@DisplayName("단체 지정 요청")
	@Test
	void create() throws Exception {
		TableGroupResponse tableGroup = new TableGroupResponse(1L, Collections.emptyList());

		when(tableGroupService.create(any(TableGroupRequest.class))).thenReturn(tableGroup);

		mockMvc.perform(
			post("/api/table-groups")
				.content("{\n"
					+ "  \"orderTables\": [\n"
					+ "    {\n"
					+ "      \"id\": 1\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 2\n"
					+ "    }\n"
					+ "  ]\n"
					+ "}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/table-groups/1"));
	}

	@DisplayName("단체 지정 해제 요청")
	@Test
	void ungroup() throws Exception {
		mockMvc.perform(delete("/api/table-groups/1"))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(tableGroupService, only()).ungroup(anyLong());
	}
}
