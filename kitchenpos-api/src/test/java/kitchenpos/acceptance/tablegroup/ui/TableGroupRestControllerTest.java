package kitchenpos.acceptance.tablegroup.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.ui.TableGroupRestController;

@WebMvcTest(TableGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class TableGroupRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private TableGroupService tableGroupService;

	private List<OrderTableResponse> orderTableResponses;

	@BeforeEach
	void setUp() {
		orderTableResponses = Arrays.asList(
			OrderTableResponse.of(1L, 5, false),
			OrderTableResponse.of(2L, 7, false));
	}

	@DisplayName("테이블그룹을 등록한다.")
	@Test
	public void create() throws Exception {
		// given
		TableGroupRequest request = TableGroupRequest.of(Arrays.asList(1L, 2L));
		TableGroupResponse response = TableGroupResponse.of(1L, LocalDateTime.now(), orderTableResponses);
		given(tableGroupService.create(any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(request)))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl("/api/table-groups" + "/" + response.getId()))
			.andExpect(jsonPath("$.id").value(response.getId()))
			.andDo(log());
	}
}
