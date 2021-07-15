package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;

@DisplayName("단체 관련 기능")
@SpringBootTest
class TableGroupRestControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private TableGroupService tableGroupService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TableGroupRestController tableGroupRestController;

	private TableGroup tableGroup;

	@BeforeEach
	void setup() {
		// MockMvc
		mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
			.addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
			.alwaysDo(print())
			.build();

		tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setCreatedDate(LocalDateTime.now());
	}

	@Test
	@DisplayName("단체 테이블을 생성할 수 있다.")
	public void create() throws Exception {
		// given
		given(tableGroupService.create(any())).willReturn(tableGroup);

		// when
		final ResultActions actions = 단체_테이블_생성_요청();

		// then
		단체_테이블_생성에_성공함(actions);
	}

	@Test
	@DisplayName("단체 테이블을 분리할 수 있다.")
	public void ungroup() throws Exception {
		// when
		final ResultActions actions = 단체_테이블_분리_요청();

		// then
		단체_테이블_분리에_성공함(actions);
	}

	private ResultActions 단체_테이블_생성_요청() throws Exception {
		return mockMvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tableGroup)));
	}

	private void 단체_테이블_생성에_성공함(ResultActions actions) throws Exception {
		actions.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/table-groups" + "/1"));
	}

	private ResultActions 단체_테이블_분리_요청() throws Exception {
		return mockMvc.perform(delete("/api/table-groups/" + tableGroup.getId())
			.contentType(MediaType.APPLICATION_JSON));
	}

	private void 단체_테이블_분리에_성공함(ResultActions actions) throws Exception {
		actions.andExpect(status().isNoContent());
	}
}
