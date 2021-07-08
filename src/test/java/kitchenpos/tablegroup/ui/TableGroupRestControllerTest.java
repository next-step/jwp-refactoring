package kitchenpos.tablegroup.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.TableGroupRestController;
import kitchenpos.utils.MockMvcControllerTest;
import kitchenpos.utils.domain.TableGroupObjects;

@DisplayName("단체지정 관리 기능")
@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest extends MockMvcControllerTest {

    private static final String DEFAULT_REQUEST_URL = "/api/table-groups";
    @MockBean
    private TableGroupService tableGroupService;
    @Autowired
    private TableGroupRestController tableGroupRestController;

    private TableGroupObjects tableGroupObjects;

    @Override
    protected Object controller() {
        return tableGroupRestController;
    }

    @BeforeEach
    void setUp() {
        tableGroupObjects = new TableGroupObjects();
    }

    @Test
    @DisplayName("단체 지정 등록을 할 수 있다.")
    void create_tableGroup() throws Exception {
        when(tableGroupService.create(any(TableGroup.class))).thenReturn(tableGroupObjects.getTableGroup1());

        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(tableGroupObjects.getTableGroup2())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(tableGroupObjects.getTableGroup1().getId()))
                .andExpect(jsonPath("orderTables.[0].id").value(tableGroupObjects.getTableGroup1().getOrderTables().get(0).getId()))
        ;
    }

    @Test
    @DisplayName("단체 지정 삭제를 할 수 있다.")
    void retrieve_tableGroupList() throws Exception {
        // when
        mockMvc.perform(delete(DEFAULT_REQUEST_URL + "/1"))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;

        // then
        verify(tableGroupService).ungroup(anyLong());
    }
}
