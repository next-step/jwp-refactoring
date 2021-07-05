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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest {
    private static final String TABLE_GROUP_API_URI = "/api/table-groups";

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    private MockMvc mockMvc;
    private TableGroup tableGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        orderTable = new OrderTable(1L, 1L, 1, true);
        tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable));
    }

    @DisplayName("단체 지정을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        given(tableGroupService.create(any())).willReturn(tableGroup);

        // when
        ResultActions actions = mockMvc.perform(post(TABLE_GROUP_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup)));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", TABLE_GROUP_API_URI + "/1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTables").isNotEmpty())
                .andExpect(jsonPath("orderTables[0].id").value(orderTable.getId()))
                .andExpect(jsonPath("orderTables[0].empty").value(orderTable.isEmpty()))
                .andExpect(jsonPath("orderTables[0].numberOfGuests").value(orderTable.getNumberOfGuests()));
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroupTest() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(delete(TABLE_GROUP_API_URI + "/{tableGroupId}", tableGroup.getId()));

        // then
        actions.andExpect(status().isNoContent());
    }
}
