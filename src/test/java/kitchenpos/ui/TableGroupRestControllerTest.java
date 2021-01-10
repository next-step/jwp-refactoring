package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("상품 컨트롤러 테스트")
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    public static final String DEFAULT_TABLE_GROUPS_URI = "/api/table-groups/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        final List<OrderTable> orderTables = new ArrayList<>();
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(2);
        orderTables.add(orderTable);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(orderTables);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() throws Exception {
        given(tableGroupService.create(any())).willReturn(tableGroup);

        final String jsonTypeTableGroup = objectMapper.writeValueAsString(tableGroup);

        mockMvc.perform(post(DEFAULT_TABLE_GROUPS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeTableGroup))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(tableGroup.getId()))
            .andExpect(jsonPath("orderTables[0].id").value(tableGroup.getOrderTables().get(0).getId()))
            .andExpect(jsonPath("orderTables[0].numberOfGuests").value(tableGroup.getOrderTables().get(0).getNumberOfGuests()));
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() throws Exception {
        mockMvc.perform(delete(DEFAULT_TABLE_GROUPS_URI + tableGroup.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
