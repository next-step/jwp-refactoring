package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TableRestController.class)
@MockMvcTestConfig
class TableRestControllerTest {
    private static final String TABLE_API_URI = "/api/tables";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    private OrderTableResponse orderTableResponse;
    private OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        orderTableResponse = new OrderTableResponse(1L, 1, false);
        orderTableRequest = new OrderTableRequest(1, false);
    }

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        given(tableService.create(any())).willReturn(orderTableResponse);

        // when
        ResultActions actions = mockMvc.perform(post(TABLE_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", TABLE_API_URI + "/1"));
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다.")
    @Test
    void changeEmptyTest() throws Exception {
        // given
        given(tableService.changeEmpty(any(), any())).willReturn(orderTableResponse);

        // when
        ResultActions actions = mockMvc.perform(put(TABLE_API_URI +"/{orderTableId}/empty", orderTableResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print());

        // then
        actions.andExpect(status().isOk());
    }

    @DisplayName("주문 테이블의 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        // given
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(orderTableResponse);

        // when
        ResultActions actions = mockMvc.perform(put(TABLE_API_URI +"/{orderTableId}/number-of-guests", orderTableResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print());

        // then
        actions.andExpect(status().isOk());
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        // given
        OrderTableResponse orderTableResponse2 = new OrderTableResponse(2L, 1, false);
        given(tableService.list()).willReturn(Arrays.asList(orderTableResponse, orderTableResponse2));

        // when
        ResultActions actions = mockMvc.perform(get(TABLE_API_URI)).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }
}
