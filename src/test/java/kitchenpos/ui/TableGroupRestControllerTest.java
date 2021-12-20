package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupRestControllerTest extends IntegrationTest {
	private static final String BASE_PATH = "/api/table-groups";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TableGroupService tableGroupService;

	@DisplayName("테이블 그룹 생성")
	@Test
	void create() throws Exception {
		//given
		List<OrderTable> orderTables = Arrays.asList(
			new OrderTable(1L),
			new OrderTable(2L));
		Map<String, Object> params = 테이블_그룹_정보(orderTables);
		TableGroup expectedTableGroup = new TableGroup(LocalDateTime.now(), orderTables);
		given(tableGroupService.create(any()))
			.willReturn(expectedTableGroup);

		//when
		MockHttpServletResponse response = mockMvc.perform(post(BASE_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(params))
		).andReturn().getResponse();

		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		TableGroup savedTableGroup = objectMapper.readValue(response.getContentAsString(), TableGroup.class);
		assertThat(savedTableGroup).isEqualTo(expectedTableGroup);
	}

	@DisplayName("테이블 그룹 해제")
	@Test
	void ungroup() throws Exception {
		//given
		Long tableGroupId = 1L;

		//when,then
		mockMvc.perform(delete(BASE_PATH+"/{tableGroupId}",tableGroupId)
			.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isNoContent());
	}

	private Map<String, Object> 테이블_그룹_정보(List<OrderTable> orderTables) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderTables", orderTables);
		return params;
	}
}
