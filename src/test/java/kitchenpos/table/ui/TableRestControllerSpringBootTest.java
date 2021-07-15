package kitchenpos.table.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("테이블 관리 기능 - SpringBootTest")
@SpringBootTest
class TableRestControllerSpringBootTest extends MockMvcControllerTest {
    public static final String DEFAULT_REQUEST_URL = "/api/tables";
    @Autowired
    private TableRestController tableRestController;

    @Override
    protected Object controller() {
        return tableRestController;
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    void retrieve_tableList1() throws Exception {
        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[1].id").value(2))
                .andExpect(jsonPath("[1].numberOfGuests").value(0))
                .andExpect(jsonPath("[1].empty").value(true))
                .andExpect(jsonPath("[7].id").value(8))
                .andExpect(jsonPath("[7].numberOfGuests").value(0))
                .andExpect(jsonPath("[7].empty").value(false))
        ;
    }

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    void save_table() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(orderTableRequest))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("numberOfGuests").value(orderTableRequest.getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableRequest.isEmpty()))
        ;
    }

    @Test
    @DisplayName("테이블을 비움 상태를 변경할 수 있다.")
    void set_emptyTable() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/1/empty")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(orderTableRequest))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("numberOfGuests").value(0))
                .andExpect(jsonPath("empty").value(false))
        ;
    }

    @Test
    @DisplayName("테이블 인원을 수정할 수 있다.")
    void change_numberOfGuests() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
        changeEmpty(4, orderTableRequest);

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/4/number-of-guests")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(orderTableRequest))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(4))
                .andExpect(jsonPath("numberOfGuests").value(orderTableRequest.getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableRequest.isEmpty()))
        ;
    }

    private void changeEmpty(int id, OrderTableRequest orderTableRequest) throws Exception {
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/" + id + "/empty")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(orderTableRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
