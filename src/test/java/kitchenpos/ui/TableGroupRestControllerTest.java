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

@DisplayName("단체 지정 관련 테스트")
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    public static final String TABLE_GROUPS_URI = "/api/table-groups";

    private TableGroup tableGroup;
    private OrderTable orderTable1;

    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(@Autowired TableGroupRestController tableGroupRestController) {
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
        orderTable1.setNumberOfGuests(5);

        tableGroup.setOrderTables(Arrays.asList(orderTable1));
    }

    public String toJsonString(TableGroup tableGroup) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tableGroup);
    }

    @Test
    @DisplayName("테이블 그룹을 생성할 수 있다.")
    public void create() throws Exception {
        // given
        given(tableGroupService.create(any())).willReturn(tableGroup);

        // when
        final ResultActions actions = mockMvc.perform(post(TABLE_GROUPS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(tableGroup)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/table-groups/1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTables").isNotEmpty())
                .andExpect(jsonPath("orderTables[0].id").value(orderTable1.getId()))
                .andExpect(jsonPath("orderTables[0].empty").value(orderTable1.isEmpty()))
                .andExpect(jsonPath("orderTables[0].numberOfGuests").value(orderTable1.getNumberOfGuests()));
    }

    @Test
    @DisplayName("테이블 그룹을 해지할 수 있다.")
    public void ungroup() throws Exception {
        // when
        final ResultActions actions = mockMvc.perform(delete(TABLE_GROUPS_URI + "/{tableGroupId}", tableGroup.getId()));

        // then
        actions.andExpect(status().isNoContent());
    }
}