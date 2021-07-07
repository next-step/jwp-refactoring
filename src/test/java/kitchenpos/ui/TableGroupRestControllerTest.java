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
import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.application.OrderServiceTest.주문테이블_생성;
import static kitchenpos.application.TableGroupServiceTest.단체_생성;
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
        setUpMockMvc();

        tableGroup = 단체_생성(1L, LocalDateTime.now());

        orderTable1 = 주문테이블_생성(1L, 2L, 2, true);

        단체에_주문테이블_등록();
    }

    @DisplayName("단체를 등록한다.")
    @Test
    void create() throws Exception{
        given(tableGroupService.create(any())).willReturn(tableGroup);

        final ResultActions resultActions = 단체_등록_요청();

        단체_요청됨(resultActions);
    }

    @DisplayName("단체를 해제한다.")
    @Test
    void ungroup() throws Exception{
        final ResultActions resultActions = 단체_해제_요청();

        단체_해제됨(resultActions);
    }

    public String toString(TableGroup tableGroup) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tableGroup);
    }
    private void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    private void 단체에_주문테이블_등록() {
        tableGroup.updateOrderTables(Arrays.asList(orderTable1));
    }

    private ResultActions 단체_등록_요청() throws Exception{
        return mockMvc.perform(post(TABLE_GROUPS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(tableGroup)));
    }


    private void 단체_요청됨(ResultActions resultActions) throws Exception{
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/table-groups/1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTables").isNotEmpty())
                .andExpect(jsonPath("orderTables[0].id").value(orderTable1.getId()))
                .andExpect(jsonPath("orderTables[0].empty").value(orderTable1.isEmpty()))
                .andExpect(jsonPath("orderTables[0].numberOfGuests").value(orderTable1.getNumberOfGuests()));

    }

    private ResultActions 단체_해제_요청() throws Exception{
        return mockMvc
                .perform(delete(TABLE_GROUPS_URI + TABLE_UNGROUPS_URI, tableGroup.getId()));
    }

    private void 단체_해제됨(ResultActions resultActions) throws Exception{
        resultActions.andExpect(status().isNoContent());
    }
}
