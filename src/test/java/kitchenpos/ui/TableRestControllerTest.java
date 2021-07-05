package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.ui.TableRestController;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TableService tableService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("테이블 생성 Api 테스트")
    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);

        String requestBody = objectMapper.writeValueAsString(orderTable);

        OrderTableResponse responseOrderTable = new OrderTableResponse();
        responseOrderTable.setId(1L);
        responseOrderTable.setEmpty(false);
        responseOrderTable.setNumberOfGuests(4);
        String responseBody = objectMapper.writeValueAsString(responseOrderTable);

        when(tableService.create(any())).thenReturn(responseOrderTable);

        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("테이블 목록 Api 테스트")
    @Test
    void list() throws Exception {
        OrderTable orderTable = new OrderTable(4, false);
        orderTable.setId(1L);

        List<OrderTableResponse> orders = Arrays.asList(OrderTableResponse.of(orderTable));

        String responseBody = objectMapper.writeValueAsString(orders);

        when(tableService.list()).thenReturn(orders);
        mockMvc.perform(get("/api/tables")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("테이블 정리 Api 테스트")
    @Test
    void changeEmpty() throws Exception {
        Long orderTableId = 1L;

        OrderTable orderTable = new OrderTable(4, false);
        orderTable.setId(1L);

        String requestBody = objectMapper.writeValueAsString(orderTable);

        OrderTableResponse responseOrderTable = new OrderTableResponse();
        responseOrderTable.setId(1L);
        responseOrderTable.setEmpty(true);
        responseOrderTable.setNumberOfGuests(0);
        String responseBody = objectMapper.writeValueAsString(responseOrderTable);

        when(tableService.changeEmpty(any(), any())).thenReturn(responseOrderTable);

        mockMvc.perform(put("/api/tables/" + orderTableId + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("테이블 인원수 변경 Api 테스트")
    @Test
    void changeNumberOfGuests() throws Exception {
        Long orderTableId = 1L;

        OrderTable orderTable = new OrderTable(4, false);
        orderTable.setId(1L);

        String requestBody = objectMapper.writeValueAsString(orderTable);

        OrderTableResponse responseOrderTable = new OrderTableResponse();
        responseOrderTable.setId(1L);
        responseOrderTable.setEmpty(false);
        responseOrderTable.setNumberOfGuests(2);
        String responseBody = objectMapper.writeValueAsString(responseOrderTable);

        when(tableService.changeNumberOfGuests(any(), any())).thenReturn(responseOrderTable);

        mockMvc.perform(put("/api/tables/" + orderTableId + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }

}
