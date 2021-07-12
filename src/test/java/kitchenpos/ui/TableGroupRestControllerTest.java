package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest extends WebMvcTestConfiguration {
	@MockBean
	TableGroupService tableGroupService;

	@Test
	void createTest() throws Exception {
		given(tableGroupService.create(any())).willReturn(new TableGroup());

		mockMvc.perform(post("/api/table-groups")
			.content(objectMapper.writeValueAsString(new TableGroup())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	void ungroupTest() throws Exception {
		Long tableGroupId = 1L;

		mockMvc.perform(delete("/api/table-groups/" + tableGroupId)
			.content(objectMapper.writeValueAsString(new TableGroup())).contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}
