package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
@DisplayName("TableRestController 클래스")
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @Nested
    @DisplayName("POST /api/tables")
    class Describe_create {

        @Nested
        @DisplayName("등록할 주문 테이블이 주어지면")
        class Context_with_order_table {
            OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                givenOrderTable.setNumberOfGuests(0);
                givenOrderTable.setEmpty(true);

                when(tableService.create(any(OrderTable.class)))
                        .thenReturn(givenOrderTable);
            }

            @DisplayName("201 Created 와 생성된 단체 지정 테이블을 응답한다.")
            @Test
            void It_responds_created_with_table_group() throws Exception {
                mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(givenOrderTable)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrderTable)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("GET /api/products 는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 주문 테이블 목록이 있으면")
        class Context_with_order_tables {
            List<OrderTable> givenOrderTables;

            @BeforeEach
            void setUp() {
                OrderTable orderTable = new OrderTable();
                givenOrderTables = Collections.singletonList(orderTable);
                when(tableService.list())
                        .thenReturn(givenOrderTables);
            }

            @DisplayName("200 OK 와 주문 테이블 목록을 응답한다.")
            @Test
            void it_responds_ok_with_order_tables() throws Exception {
                mockMvc.perform(get("/api/tables"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrderTables)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("Put /api/tables/{orderTableId}/empty 는")
    class Describe_changeEmpty {

        @Nested
        @DisplayName("빈 상태로 만들 주문 테이블 식별자와 주문 테이블이 주어지면")
        class Context_with_order_table_id_and_order_table {
            Long givenOrderTableId = 1L;
            OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                givenOrderTable.setEmpty(true);

                when(tableService.changeEmpty(anyLong(), any(OrderTable.class)))
                        .thenReturn(givenOrderTable);
            }

            @DisplayName("200 OK 상태와 갱신된 주문 테이블 정보를 응답한다.")
            @Test
            void It_responds_updated_order_table() throws Exception {
                mockMvc.perform(put("/api/tables/{orderTableId}/empty", givenOrderTableId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(givenOrderTable)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrderTable)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("Put /api/tables/{orderTableId}/number-of-guests 는")
    class Describe_changeNumberOfGuests {

        @Nested
        @DisplayName("빈 상태로 만들 주문 테이블 식별자와 주문 테이블이 주어지면")
        class Context_with_order_table_id_and_order_table {
            Long givenOrderTableId = 1L;
            OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                givenOrderTable.setNumberOfGuests(2);

                when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class)))
                        .thenReturn(givenOrderTable);
            }

            @DisplayName("200 OK 상태와 갱신된 주문 테이블 정보를 응답한다.")
            @Test
            void It_responds_updated_order_table() throws Exception {
                mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", givenOrderTableId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(givenOrderTable)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrderTable)
                        ));
            }
        }
    }
}
