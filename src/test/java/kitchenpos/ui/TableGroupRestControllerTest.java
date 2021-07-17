package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.TableGroupService;
import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupRestControllerTest extends BaseControllerTest {

    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup 테이블그룹_1번_2번;
    OrderTable 테이블_1번, 테이블_2번;

    @BeforeEach
    void setUp() {
        테이블_1번 = new OrderTable();
        테이블_1번.setId(1L);
        테이블_1번.setEmpty(true);

        테이블_2번 = new OrderTable();
        테이블_2번.setId(2L);
        테이블_2번.setEmpty(true);

        테이블그룹_1번_2번 = new TableGroup();
        테이블그룹_1번_2번.setId(1L);
        테이블그룹_1번_2번.setOrderTables(Arrays.asList(테이블_1번, 테이블_2번));
    }
    
    @DisplayName("단체 지정")
    @Test
    void create() throws Exception {
        when(tableGroupService.create(any())).thenReturn(테이블그룹_1번_2번);
        String jsonString = objectMapper.writeValueAsString(테이블그룹_1번_2번);

        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonString)
        ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(테이블그룹_1번_2번.getId()));
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/" + 테이블그룹_1번_2번.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}