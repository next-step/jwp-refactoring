package kitchenpos.tablegroup.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("단체지정 관리 기능")
@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest extends MockMvcControllerTest {

    private static final String DEFAULT_REQUEST_URL = "/api/table-groups";
    @MockBean
    private TableGroupService tableGroupService;
    @Autowired
    private TableGroupRestController tableGroupRestController;

    @Override
    protected Object controller() {
        return tableGroupRestController;
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("단체 지정 등록을 할 수 있다.")
    void create_tableGroup1() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(4L));
        OrderTable orderTable = new OrderTable(4, false);
        TableGroupResponse tableGroupResponse = TableGroupResponse.of(1L, LocalDateTime.now());
        given(tableGroupService.create(any(TableGroupRequest.class))).willReturn(tableGroupResponse);

        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(tableGroupRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(tableGroupResponse.getId()))
        ;
    }

    @Test
    @DisplayName("단체 지정 삭제를 할 수 있다.")
    void retrieve_tableGroupList1() throws Exception {
        // when
        mockMvc.perform(delete(DEFAULT_REQUEST_URL + "/1"))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;

        // then
        verify(tableGroupService).ungroup(anyLong());
    }
}
