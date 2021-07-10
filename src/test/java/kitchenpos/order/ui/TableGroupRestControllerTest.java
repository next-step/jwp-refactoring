package kitchenpos.order.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.ui.TableGroupRestController;
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
import java.util.List;

//import static kitchenpos.order.application.OrderServiceTest.주문테이블_생성;
//import static kitchenpos.order.application.TableGroupServiceTest.단체_생성;
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

    private TableGroupResponse tableGroupResponse;
    private OrderTable orderTable1;
    private TableGroup tableGroup;

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
        tableGroupResponse = 테이블_그룹_응답값_생성();
        orderTable1 = new OrderTable(1L, null, 3, true);
    }

    @DisplayName("단체를 등록한다.")
    @Test
    void create() throws Exception{
        given(tableGroupService.create(any())).willReturn(tableGroupResponse);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(1L, 주문_테이블_리스트_생성());

        final ResultActions resultActions = 단체_등록_요청(tableGroupRequest);

        단체_요청됨(resultActions);
    }

    @DisplayName("단체를 해제한다.")
    @Test
    void ungroup() throws Exception{
        final ResultActions resultActions = 단체_해제_요청();

        단체_해제됨(resultActions);
    }

    private TableGroupResponse 테이블_그룹_응답값_생성() {
        OrderTable orderTable = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(1L, null,4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        tableGroup = new TableGroup(1L, orderTables);
        return new TableGroupResponse(tableGroup);
    }

    private void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    private List<OrderTableRequest> 주문_테이블_리스트_생성() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, null, 3, false);
        return Arrays.asList(orderTableRequest);
    }

    private ResultActions 단체_등록_요청(TableGroupRequest request) throws Exception{
        return mockMvc.perform(post(TABLE_GROUPS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }


    private void 단체_요청됨(ResultActions resultActions) throws Exception{
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/table-groups/1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTableResponseList").isNotEmpty())
                .andExpect(jsonPath("orderTableResponseList[0].id").value(orderTable1.getId()))
                .andExpect(jsonPath("orderTableResponseList[0].empty").value(orderTable1.getEmpty()))
                .andExpect(jsonPath("orderTableResponseList[0].numberOfGuests").value(orderTable1.getNumberOfGuests()));

    }

    private ResultActions 단체_해제_요청() throws Exception{
        return mockMvc
                .perform(delete(TABLE_GROUPS_URI + TABLE_UNGROUPS_URI, tableGroup.getId()));
    }

    private void 단체_해제됨(ResultActions resultActions) throws Exception{
        resultActions.andExpect(status().isNoContent());
    }
}
