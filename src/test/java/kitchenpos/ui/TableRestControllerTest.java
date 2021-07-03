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
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(true);
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() throws Exception{
        given(tableService.create(any())).willReturn(orderTable1);

        final ResultActions resultActions = mockMvc.perform(post(ORDER_TABLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(orderTable1)));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tableGroupId").isEmpty())
                .andExpect(jsonPath("$.numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$.empty").value(orderTable1.isEmpty()));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() throws Exception{
        given(tableService.list()).willReturn(Arrays.asList(orderTable1, orderTable2));

        final ResultActions resultActions = mockMvc.perform(get(ORDER_TABLE_URI)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].tableGroupId").isEmpty())
                .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$[0].empty").value(orderTable1.isEmpty()));
    }

    @DisplayName("주문 테이블을 비운다.")
    @Test
    void changeEmpty() throws Exception{
        given(tableService.changeEmpty(orderTable1.getId(), orderTable1)).willReturn(orderTable1);

        final ResultActions resultActions = mockMvc.perform(put(ORDER_TABLE_URI +ORDER_TABLE_EMPTY_URL, orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(orderTable1)));

        resultActions.andExpect(status().isOk());
    }

    @DisplayName("주문 테이블의 손님 수를 입력한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        given(tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1)).willReturn(orderTable1);

        final ResultActions resultActions = mockMvc.perform(put(ORDER_TABLE_URI +ORDER_TABLE_CHANGE_GUEST_NUMBER_URL, orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(orderTable1)));

        resultActions.andExpect(status().isOk());
    }

    public String toString(OrderTable orderTable) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderTable);
    }
}