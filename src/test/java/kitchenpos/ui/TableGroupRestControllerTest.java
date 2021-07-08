package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.api.TableGroupRestController;
import org.assertj.core.util.Lists;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TableGroupRestController.class)
class TableGroupRestControllerTest {
    private static final String URI = "/api/table-groups";

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private TableGroup tableGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        orderTable = OrderTable.of(1L, 1L, 4, true);
        tableGroup = TableGroup.of(1L, LocalDateTime.now(), Lists.list(orderTable));
    }

    @DisplayName("테이블 그룹을 추가한다.")
    @Test
    void create() throws Exception {
        //given
        given(tableGroupService.create(any())).willReturn(tableGroup);

        //when
        ResultActions actions = mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI + "/1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTables").isNotEmpty())
                .andExpect(jsonPath("orderTables[0].id").value(orderTable.getId()))
                .andExpect(jsonPath("orderTables[0].tableGroupId").value(orderTable.getTableGroupId()))
                .andExpect(jsonPath("orderTables[0].numberOfGuests").value(orderTable.getNumberOfGuests()))
                .andExpect(jsonPath("orderTables[0].empty").value(orderTable.isEmpty()));
    }

    @DisplayName("특정 테이블 그룹을 삭제한다.")
    @Test
    void ungroup() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(delete(URI + "/{tableGroupId}", tableGroup.getId()));

        //then
        actions.andExpect(status().isNoContent());
    }

    @DisplayName("테이블 그룹을 추가한다.2")
    @Test
    void create2() throws Exception {
        //given
        given(tableGroupService.create(any())).willReturn(tableGroup);

        //when
        ResultActions actions = mockMvc.perform(post(URI +"2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI + "2/1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTables").isNotEmpty())
                .andExpect(jsonPath("orderTables[0].id").value(orderTable.getId()))
                .andExpect(jsonPath("orderTables[0].tableGroupId").value(orderTable.getTableGroupId()))
                .andExpect(jsonPath("orderTables[0].numberOfGuests").value(orderTable.getNumberOfGuests()))
                .andExpect(jsonPath("orderTables[0].empty").value(orderTable.isEmpty()));
    }
}
