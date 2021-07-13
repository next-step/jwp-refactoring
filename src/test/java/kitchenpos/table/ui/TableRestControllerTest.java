package kitchenpos.table.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.utils.domain.OrderTableObjects;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("테이블 관리 기능")
@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest extends MockMvcControllerTest {

    public static final String DEFAULT_REQUEST_URL = "/api/tables";
    @MockBean
    private TableService tableService;
    @Autowired
    private TableRestController tableRestController;

    private OrderTableObjects orderTableObjects;

    @Override
    protected Object controller() {
        return tableRestController;
    }

    @BeforeEach
    void setUp() {
        orderTableObjects = new OrderTableObjects();
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    void retrieve_tableList1() throws Exception {
        // given
        OrderTable orderTable = orderTableObjects.getOrderTable1();
        OrderTableResponse orderTableResponse = OrderTableResponse.of(1L, orderTable.getTableGroupId(), orderTable.getNumberOfGuests().toInt(), orderTable.isEmpty());
        given(tableService.list()).willReturn(new ArrayList<>(Arrays.asList(orderTableResponse)));

        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(orderTableResponse.getId()))
                .andExpect(jsonPath("[0].numberOfGuests").value(orderTableResponse.getNumberOfGuests()))
                .andExpect(jsonPath("[0].empty").value(orderTableResponse.isEmpty()))
        ;
    }

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    void save_table() throws Exception {
        // given
        OrderTableResponse orderTableResponse = OrderTableResponse.of(1L, 1L, 3, false);
        given(tableService.create(any(OrderTableRequest.class))).willReturn(orderTableResponse);

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(orderTableObjects.getOrderTableRequest1()))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("numberOfGuests").value(orderTableResponse.getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableResponse.isEmpty()))
        ;
    }

    @Test
    @DisplayName("테이블을 비울 수 있다.")
    void set_emptyTable() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
        OrderTableResponse orderTableResponse = OrderTableResponse.of(1L, 1L, 3, false);
        given(tableService.changeEmpty(anyLong(), any(OrderTableRequest.class))).willReturn(orderTableResponse);

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/1/empty")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(orderTableRequest))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(orderTableResponse.getId()))
                .andExpect(jsonPath("numberOfGuests").value(orderTableResponse.getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableResponse.isEmpty()))
        ;
    }

    @Test
    @DisplayName("테이블 인원을 수정할 수 있다.")
    void change_numberOfGuests() throws Exception {
        // given
        OrderTableResponse orderTableResponse = OrderTableResponse.of(1L, 1L, 4, false);
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTableRequest.class))).willReturn(orderTableResponse);

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/4/number-of-guests")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(orderTableObjects.getOrderTableRequest1()))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(orderTableResponse.getId()))
                .andExpect(jsonPath("numberOfGuests").value(orderTableResponse.getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableResponse.isEmpty()))
        ;
    }
}
