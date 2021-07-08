package kitchenpos.table.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.domain.OrderTableObjects;
import kitchenpos.ui.TableRestController;
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
    void retrieve_tableList() throws Exception {
        // given
        when(tableService.list()).thenReturn(orderTableObjects.getOrderTables());

        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(orderTableObjects.getOrderTable1().getId()))
                .andExpect(jsonPath("[0].numberOfGuests").value(orderTableObjects.getOrderTable1().getNumberOfGuests()))
                .andExpect(jsonPath("[0].empty").value(orderTableObjects.getOrderTable1().isEmpty()))
                .andExpect(jsonPath("[7].id").value(orderTableObjects.getOrderTable8().getId()))
                .andExpect(jsonPath("[7].numberOfGuests").value(orderTableObjects.getOrderTable8().getNumberOfGuests()))
                .andExpect(jsonPath("[7].empty").value(orderTableObjects.getOrderTable8().isEmpty()))
        ;
    }

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    void save_table() throws Exception {
        // given
        orderTableObjects.getOrderTable1().setTableGroupId(null);
        when(tableService.create(any(OrderTable.class))).thenReturn(orderTableObjects.getOrderTable1());

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderTableObjects.getOrderTable2()))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(orderTableObjects.getOrderTable1().getId()))
                .andExpect(jsonPath("numberOfGuests").value(orderTableObjects.getOrderTable1().getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableObjects.getOrderTable1().isEmpty()))
        ;
    }

    @Test
    @DisplayName("테이블을 비울 수 있다.")
    void set_emptyTable() throws Exception {
        // given
        when(tableService.changeEmpty(anyLong(), any(OrderTable.class))).thenReturn(orderTableObjects.getOrderTable1());

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/1/empty")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(orderTableObjects.getOrderTable1()))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(orderTableObjects.getOrderTable1().getId()))
                .andExpect(jsonPath("numberOfGuests").value(orderTableObjects.getOrderTable1().getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableObjects.getOrderTable1().isEmpty()))
        ;
    }

    @Test
    @DisplayName("테이블 인원을 수정할 수 있다.")
    void change_numberOfGuests() throws Exception {
        // given
        orderTableObjects.getOrderTable4().setNumberOfGuests(4);
        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).thenReturn(orderTableObjects.getOrderTable4());

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/4/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(orderTableObjects.getOrderTable1()))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(orderTableObjects.getOrderTable4().getId()))
                .andExpect(jsonPath("numberOfGuests").value(orderTableObjects.getOrderTable4().getNumberOfGuests()))
                .andExpect(jsonPath("empty").value(orderTableObjects.getOrderTable4().isEmpty()))
        ;
    }
}
