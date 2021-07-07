package kitchenpos.ui.order;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.TableGroupRestController;

@DisplayName("단체 지정 컨트롤러 테스트")
@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TableGroupService tableGroupService;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();
	}

	@DisplayName("create")
	@Test
	public void create() throws Exception {
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), null);
		given(tableGroupService.create(any())).willReturn(tableGroup);

		mockMvc.perform(post("/api/table-groups")
			.content(objectMapper.writeValueAsString(tableGroup))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", "/api/table-groups/" + tableGroup.getId()))
		;
	}

	@DisplayName("ungroup")
	@Test
	public void ungroup() throws Exception {
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), null);

		mockMvc.perform(delete("/api/table-groups/" + tableGroup.getId()))
			.andExpect(status().isNoContent())
		;
	}
}
