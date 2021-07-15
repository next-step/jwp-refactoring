package kitchenpos.tablegroup.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("단체지정 관리 기능 - SpringBootTest")
@SpringBootTest
class TableGroupRestControllerSpringBootTest extends MockMvcControllerTest {
    private static final String DEFAULT_REQUEST_URL = "/api/table-groups";
    @Autowired
    private TableGroupRestController tableGroupRestController;

    @Override
    protected Object controller() {
        return tableGroupRestController;
    }

    @Test
    @DisplayName("단체 지정 등록을 할 수 있다.")
    void create_tableGroup() throws Exception {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(2L, 3L));

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(tableGroupRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName("단체 지정 삭제를 할 수 있다.")
    void retrieve_tableGroupList() throws Exception {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(5L, 6L));
        groupingTables(tableGroupRequest);

        // when
        mockMvc.perform(delete(DEFAULT_REQUEST_URL + "/1"))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
    }

    private void groupingTables(TableGroupRequest tableGroupRequest) throws Exception {
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(tableGroupRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }
}
