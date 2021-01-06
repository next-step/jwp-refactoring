package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.Order;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TableGroupRestControllerTest {
    private TableGroupRestController tableGroupRestController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TableGroupService tableGroupService;

    @BeforeEach
    void setup() {
        tableGroupRestController = new TableGroupRestController(tableGroupService);

        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController).build();

        objectMapper = new ObjectMapper();
    }

    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void createTableGroupTest() throws Exception {
        // given
        String url = "/api/table-groups";
        TableGroup tableGroupRequest = new TableGroup();
        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setId(1L);
        given(tableGroupService.create(any())).willReturn(savedTableGroup);

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + savedTableGroup.getId()))
        ;
    }

    @DisplayName("단체 지정을 제거할 수 있다.")
    @Test
    void deleteTableGroupTest() throws Exception {
        // given
        Long targetId = 1L;
        String url = "/api/table-groups/" + targetId;

        // when, then
        mockMvc.perform(delete(url))
                .andExpect(status().isNoContent())
        ;

        verify(tableGroupService).ungroup(targetId);
    }
}