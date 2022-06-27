package kitchenpos.tablegroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.ui.TableGroupRestController;
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
        OrderTable orderTable1 = new OrderTable(1L, null, 5, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        TableGroup tableGroup = new TableGroup(new OrderTables(2, Arrays.asList(orderTable1, orderTable2)));
        given(tableGroupService.create(any())).willReturn(new TableGroupResponse(tableGroup));

        //then
        mockMvc.perform(post("/api/table-groups").content(objectMapper.writeValueAsString(new TableGroupRequest(
                                Arrays.asList(new OrderTableRequest(), new OrderTableRequest()))))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
