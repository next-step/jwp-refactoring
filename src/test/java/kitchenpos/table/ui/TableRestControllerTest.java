package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TableService tableService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @DisplayName("(주문)테이블을 등록할 수 있다.")
    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable(2, true);
        String params = mapper.writeValueAsString(orderTable);
        given(tableService.create(any())).willReturn(OrderTableResponse.from(orderTable));

        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(params))
                .andExpect(status().isCreated());
    }

    @DisplayName("(주문)테이블을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(2, true));
        orderTables.add(new OrderTable(3, true));

        List<OrderTableResponse> expectedTables = orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        given(tableService.list()).willReturn(expectedTables);

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk());
    }

    @DisplayName("(주문)테이블의 empty 값을 변경할 수 있다.")
    @Test
    void changeEmpty() throws Exception {
        OrderTable orderTable = new OrderTable(1L, 1L, 2, true);
        OrderTable changedTable = new OrderTable(1L, 1L, 2, false);
        String orderTableJsonString = mapper.writeValueAsString(orderTable);

        given(tableService.changeEmpty(anyLong(), any())).willReturn(OrderTableResponse.from(changedTable));

        mockMvc.perform(put("/api/tables/" + orderTable.getId() + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderTableJsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }

    @DisplayName("(주문)테이블의 guests(손님) 숫자를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable orderTable = new OrderTable(1L, 1L, 2, true);
        OrderTable changedTable = new OrderTable(1L, 1L, 4, false);
        String orderTableJsonString = mapper.writeValueAsString(orderTable);

        given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(OrderTableResponse.from(changedTable));

        mockMvc.perform(put("/api/tables/" + orderTable.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderTableJsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests").value(4));
    }
}
