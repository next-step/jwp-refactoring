package kitchenpos.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.api.TableGroupRestController;
import kitchenpos.service.table.dto.OrderTableRequest;
import kitchenpos.service.tablegroup.application.TableGroupService;
import kitchenpos.service.tablegroup.dto.TableGroupRequest;
import kitchenpos.service.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

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
        given(tableGroupService.create(any())).willReturn(new TableGroupResponse());

        //then
        mockMvc.perform(post("/api/table-groups").content(objectMapper.writeValueAsString(new TableGroupRequest(
                                Arrays.asList(new OrderTableRequest(), new OrderTableRequest()))))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
