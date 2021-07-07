package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
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

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TableGroupRestController tableGroupRestController;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    TableGroupService tableGroupService;

    TableGroup 테이블그룹;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        테이블그룹 = new TableGroup();
        테이블그룹.setId(1L);

    }

    @Test
    @DisplayName("단체 테이블을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(테이블그룹);
        when(tableGroupService.create(any())).thenReturn(테이블그룹);

        //when
        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"id\":1")));
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup() throws Exception {
        //when && then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 테이블그룹.getId()))
                .andExpect(status().isNoContent());
    }
}