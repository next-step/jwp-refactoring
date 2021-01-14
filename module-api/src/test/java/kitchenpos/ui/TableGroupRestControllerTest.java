package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.tablegroup.OrderTableInTableGroupRequest;
import dto.tablegroup.TableGroupRequest;
import dto.tablegroup.TableGroupResponse;
import kitchenpos.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

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
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(1L), new OrderTableInTableGroupRequest(2L)));
        TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, LocalDateTime.now(), new ArrayList<>());
        given(tableGroupService.group(tableGroupRequest)).willReturn(tableGroupResponse);

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + tableGroupResponse.getId()))
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