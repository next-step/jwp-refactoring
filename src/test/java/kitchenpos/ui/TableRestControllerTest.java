package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static kitchenpos.application.TableServiceTest.주문테이블_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 테이블 관련 기능 테스트")
@SpringBootTest
class TableRestControllerTest {
    private static final String ORDER_TABLE_URI = "/api/tables";
    private static final String ORDER_TABLE_EMPTY_URL = "/{orderTableId}/empty";
    private static final String ORDER_TABLE_CHANGE_GUEST_NUMBER_URL = "/{orderTableId}/number-of-guests";

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TableRestController tableRestController;

    @BeforeEach
    void setUp() {
        setUpMockMvc();

        orderTable1 = 주문테이블_생성(1L, 0, false);

        orderTable2 = 주문테이블_생성(2L, 0, false);
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() throws Exception{
        final ResultActions resultActions = 주문테이블_등록_요청();

        주문테이블_등록됨(resultActions);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() throws Exception{
        final ResultActions resultActions = 주문테이블_목록_조회_요청();

        주문테이블_목록_조회됨(resultActions);
    }

    @DisplayName("주문 테이블을 비운다.")
    @Test
    void changeEmpty() throws Exception{
        final ResultActions resultActions = 주문테이블_비움_요청();

        주문테이블_비워짐(resultActions);
    }

    @DisplayName("주문 테이블의 손님 수를 입력한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        final ResultActions resultActions = 주문테이블_손님수_변경_요청();

        주문테이블_손님_수_변경됨(resultActions);
    }

    public String toString(OrderTable orderTable) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderTable);
    }

    private void setUpMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

    }

    private ResultActions 주문테이블_등록_요청() throws Exception {
        given(tableService.create(any())).willReturn(orderTable1);
        return mockMvc.perform(post(ORDER_TABLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(orderTable1)));
    }

    private void 주문테이블_등록됨(ResultActions resultActions) throws Exception{
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tableGroupId").isEmpty())
                .andExpect(jsonPath("$.numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$.empty").value(orderTable1.isEmpty()));
    }

    private ResultActions 주문테이블_목록_조회_요청() throws Exception{
        given(tableService.list()).willReturn(Arrays.asList(orderTable1, orderTable2));
        return mockMvc.perform(get(ORDER_TABLE_URI)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void 주문테이블_목록_조회됨(ResultActions resultActions) throws Exception{
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].tableGroupId").isEmpty())
                .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$[0].empty").value(orderTable1.isEmpty()));
    }

    private ResultActions 주문테이블_비움_요청() throws Exception {
        orderTable1.updateEmpty(true);
        given(tableService.changeEmpty(orderTable1.getId(), orderTable1)).willReturn(orderTable1);
        return mockMvc.perform(put(ORDER_TABLE_URI +ORDER_TABLE_EMPTY_URL, orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(orderTable1)));
    }

    private void 주문테이블_비워짐(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }

    private ResultActions 주문테이블_손님수_변경_요청() throws Exception {
        orderTable1.updateNumberOfGuests(10);
        given(tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1)).willReturn(orderTable1);
        return mockMvc.perform(put(ORDER_TABLE_URI +ORDER_TABLE_CHANGE_GUEST_NUMBER_URL, orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(orderTable1)));
    }

    private void 주문테이블_손님_수_변경됨(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk());
    }
}