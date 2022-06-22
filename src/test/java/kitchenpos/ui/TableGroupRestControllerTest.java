package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TableGroupRestControllerTest {
    private MockMvc mockMvc;
    @Mock
    private TableGroupService tableGroupService;
    @InjectMocks
    private TableGroupRestController tableGroupRestController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController).build();
    }

    @Test
    void test_delete() throws Exception {
        //given
        doNothing().when(tableGroupService).ungroup(any());

        //then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 0))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void test_post() throws Exception {
        //given
        given(tableGroupService.create(any())).willReturn(new TableGroupResponse(new TableGroup()));

        //then
        mockMvc.perform(post("/api/table-groups").content(objectMapper.writeValueAsString(new MenuGroup("menuGroup")))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
