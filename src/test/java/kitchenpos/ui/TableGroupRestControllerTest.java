package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private TableGroupService tableGroupService;

	@DisplayName("테이블그룹을 등록한다.")
	@Test
	public void create() throws Exception {
		// given
		OrderTable table1 = OrderTable.of(1L, null, 0, true);
		OrderTable table2 = OrderTable.of(2L, null, 5, false);
		List<OrderTable> orderTables = Arrays.asList(table1, table2);
		TableGroup expectedTableGroup = TableGroup.of(1L, null, orderTables);
		given(tableGroupService.create(any())).willReturn(expectedTableGroup);

		// when
		final ResultActions resultActions = mvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(expectedTableGroup)))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl("/api/table-groups" + "/" + expectedTableGroup.getId()))
			.andExpect(jsonPath("$.id").value(expectedTableGroup.getId()))
			.andDo(log());
	}
}
