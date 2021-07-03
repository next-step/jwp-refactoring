package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("단체 그룹 관련 기능 테스트")
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    private static final String TABLE_GROUPS_URI = "/api/table-groups";
    private static final String TABLE_UNGROUPS_URI = "/{tableGroupId}";

    private TableGroup tableGroup;
    private OrderTable orderTable1;

    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @BeforeEach
    void setUp() {
        // MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        tableGroup = new TableGroup();
        tableGroup.setId(1L);

        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(1L);
        orderTable1.setNumberOfGuests(2);

        tableGroup.setOrderTables(Arrays.asList(orderTable1));
    }

    @DisplayName("단체를 등록한다.")
    @Test
    void create() throws Exception{
        given(tableGroupService.create(any())).willReturn(tableGroup);

        final ResultActions resultActions = mockMvc.perform(post(TABLE_GROUPS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(tableGroup)));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/table-groups/1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTables").isNotEmpty())
                .andExpect(jsonPath("orderTables[0].id").value(orderTable1.getId()))
                .andExpect(jsonPath("orderTables[0].empty").value(orderTable1.isEmpty()))
                .andExpect(jsonPath("orderTables[0].numberOfGuests").value(orderTable1.getNumberOfGuests()));
    }

    @DisplayName("단체를 해제한다.")
    @Test
    void ungroup() throws Exception{
        final ResultActions resultActions = mockMvc
                .perform(delete(TABLE_GROUPS_URI + TABLE_UNGROUPS_URI, tableGroup.getId()));

        resultActions.andExpect(status().isNoContent());
    }

    public String toString(TableGroup tableGroup) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tableGroup);
    }
}
