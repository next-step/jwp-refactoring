package kitchenpos.ui;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 테이블 Controller Test")
class TableRestControllerTest extends RestControllerTest {

    public static final String TABLES_URL = "/api/tables";

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() throws Exception {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(false);

        //when
        //then
        mockMvc.perform(
                post(TABLES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(orderTable))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern(TABLES_URL + "/*"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.numberOfGuests", is(3)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        mockMvc.perform(get(TABLES_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(8)))
                .andExpect(jsonPath("$[0]['id']", is(1)))
                .andExpect(jsonPath("$[0]['numberOfGuests']", is(4)))
                .andExpect(jsonPath("$[1]['id']", is(2)))
                .andExpect(jsonPath("$[1]['numberOfGuests']", is(0)))
                .andExpect(jsonPath("$[2]['id']", is(3)))
                .andExpect(jsonPath("$[2]['numberOfGuests']", is(2)))
                .andExpect(jsonPath("$[3]['id']", is(4)))
                .andExpect(jsonPath("$[3]['numberOfGuests']", is(0)))
                .andExpect(jsonPath("$[4]['id']", is(5)))
                .andExpect(jsonPath("$[4]['numberOfGuests']", is(0)))
                .andExpect(jsonPath("$[5]['id']", is(6)))
                .andExpect(jsonPath("$[5]['numberOfGuests']", is(0)));
    }

    @DisplayName("주문 테이블을 비울수 있다.")
    @Test
    void changeEmpty() throws Exception {
        //given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("empty", "true");

        //when
        //then
        mockMvc.perform(
                put(String.format(TABLES_URL + "/%s/empty", 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(requestBody))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.empty", is(true)));
    }

    @DisplayName("주문 테이블의 방문한 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        //given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("numberOfGuests", "3");

        //when
        //then
        mockMvc.perform(
                put(String.format(TABLES_URL + "/%s/number-of-guests", 1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(requestBody))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numberOfGuests", is(3)));
    }
}
