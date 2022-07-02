package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.ui.TableGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TableGroupRestControllerTest {
    private static final String URI = "/api/table-groups";

    @InjectMocks
    private ObjectMapper objectMapper;
    @InjectMocks
    private TableGroupRestController tableGroupRestController;

    @Mock
    private TableGroupService tableGroupService;

    private MockMvc mockMvc;
    private TableGroupResponse 단체_테이블_응답;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController).build();
        단체_테이블_응답 = new TableGroupResponse(1L, null, new ArrayList<>());
    }

    @Test
    void post() throws Exception {
        // given
        given(tableGroupService.create(any())).willReturn(단체_테이블_응답);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(단체_테이블_응답)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        // given
        doNothing().when(tableGroupService).ungroup(anyLong());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(URI + "/{tableGroupId}", 단체_테이블_응답.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
