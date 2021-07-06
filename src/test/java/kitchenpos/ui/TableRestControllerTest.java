package kitchenpos.ui;

import kitchenpos.ApiTest;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ApiTest {

    @MockBean
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void createTest() throws Exception {

        // given
        when(tableService.create(any())).thenReturn(orderTable);

        // then
        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderTable)))
                .andExpect(status().isCreated());
    }
    
    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void listTest() throws Exception {

        // given
        when(tableService.list()).thenReturn(Collections.singletonList(orderTable));

        // then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                ;
    }
    
    @Test
    @DisplayName("주문 테이블 공석 여부를 변경한다")
    void changeEmptyTest() throws Exception {

        // given
        orderTable.setEmpty(true);
        when(tableService.changeEmpty(any(), any())).thenReturn(orderTable);

        // then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderTable)))
                .andExpect(status().isOk())
                .andExpect((content().string(containsString("true"))))
        ;
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다")
    void changeNumberOfGuestsTest() throws Exception {

        // given
        orderTable.setNumberOfGuests(3);
        when(tableService.changeNumberOfGuests(any(), any())).thenReturn(orderTable);

        // then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderTable)))
                .andExpect(status().isOk())
                .andExpect((content().string(containsString("3"))))
        ;
    }
}