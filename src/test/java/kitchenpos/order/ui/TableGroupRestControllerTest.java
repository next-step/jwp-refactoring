package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TableGroupRestController ui 테스트")
@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest extends ControllerTest {
    @MockBean
    private TableGroupService tableGroupService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private TableGroup 단체테이블;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        테이블1 = new OrderTable(1L, 1, true);
        테이블2 = new OrderTable(2L, 2, true);
        테이블3 = new OrderTable(3L, 3, true);

        단체테이블 = new TableGroup(1L, Lists.newArrayList(테이블1, 테이블2, 테이블3));
    }

    @DisplayName("단체 테이블 그룹 api 테스트")
    @Test
    void createTableGroup() throws Exception {
        given(tableGroupService.create(any(TableGroup.class))).willReturn(단체테이블);

        mockMvc.perform(post("/api/table-groups")
                        .content(mapper.writeValueAsBytes(단체테이블))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @DisplayName("단체테이블 해체 api 테스트")
    @Test
    void ungroupTables() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 단체테이블.getId()))
                .andExpect(status().isNoContent());
    }
}
