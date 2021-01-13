package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ordertable.ChangeEmptyRequest;
import dto.ordertable.ChangeNumberOfGuestsRequest;
import dto.ordertable.OrderTableRequest;
import dto.ordertable.OrderTableResponse;
import kitchenpos.OrderTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderTableRestControllerTest {
    private OrderTableRestController orderTableRestController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private OrderTableService orderTableService;

    @BeforeEach
    void setup() {
        orderTableRestController = new OrderTableRestController(orderTableService);

        mockMvc = MockMvcBuilders.standaloneSetup(orderTableRestController).build();

        objectMapper = new ObjectMapper();
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTableTest() throws Exception {
        // given
        String url = "/api/order-tables";
        int numberOfGuests = 30;
        boolean empty = false;
        Long orderTableId = 1L;
        Long tableGroupId = null;

        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, empty);

        given(orderTableService.create(orderTableRequest))
                .willReturn(new OrderTableResponse(orderTableId, null, numberOfGuests, empty));

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + orderTableId));
    }

    @DisplayName("주문 테이블 목록을 불러올 수 있다.")
    @Test
    void getOrderTablesTest() throws Exception {
        // given
        String url = "/api/order-tables";

        OrderTableResponse orderTableResponse1 = new OrderTableResponse(1L, null,3, false);
        OrderTableResponse orderTableResponse2 = new OrderTableResponse(2L, null,3, false);

        given(orderTableService.list()).willReturn(Arrays.asList(orderTableResponse1, orderTableResponse2));

        // when, then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("주문 테이블의 공석 여부를 바꿀 수 있다.")
    @Test
    void changeEmptyTest() throws Exception {
        // given
        Long targetId = 1L;
        String url = "/api/order-tables/" + targetId + "/empty";
        boolean empty = false;
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(empty);

        given(orderTableService.changeEmpty(targetId, changeEmptyRequest))
                .willReturn(new OrderTableResponse(targetId, null,5, empty));

        // when, then
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeEmptyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.empty", is(empty)));
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 바꿀 수 있다.")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        // given
        Long targetId = 1L;
        String url = "/api/order-tables/" + targetId + "/number-of-guests";
        int numberOfGuests = 300;
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(numberOfGuests);

        given(orderTableService.changeNumberOfGuests(targetId, changeNumberOfGuestsRequest))
                .willReturn(new OrderTableResponse(targetId, null, numberOfGuests, false));

        // when, then
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeNumberOfGuestsRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.numberOfGuests", is(numberOfGuests)));
    }
}