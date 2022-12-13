package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TableRestController.class)
@DisplayName("TableRestController 클래스 테스트")
public class TableRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TableRestController tableRestController;

    @Nested
    @DisplayName("POST /api/tables")
    public class PostMethod {
        @Test
        @DisplayName("성공적으로 주문 테이블을 등록하면 201 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final OrderTable orderTable = setupSuccess();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/tables")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderTable))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

            // then
            final OrderTable orderTableResponse =
                    objectMapper.readValue(response.getContentAsString(), OrderTable.class);
            assertThat(orderTableResponse.getId()).isEqualTo(orderTable.getId());
        }

        private OrderTable setupSuccess() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            final URI uri = URI.create("/api/tables/" + orderTable.getId());
            Mockito.when(tableRestController.create(Mockito.any()))
                    .thenReturn(ResponseEntity.created(uri).body(orderTable));
            return orderTable;
        }

        @Test
        @DisplayName("주문 테이블을 등록하는데 실패하면 400 상태 코드를 응답받는다")
        public void badRequest() throws Exception {
            // given
            final OrderTable orderTable = setupBadRequest();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/tables")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderTable))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private OrderTable setupBadRequest() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            final URI uri = URI.create("/api/tables/" + orderTable.getId());
            Mockito.when(tableRestController.create(Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
            return orderTable;
        }
    }

    @Nested
    @DisplayName("GET /api/tables")
    public class GetMethod {
        @Test
        @DisplayName("성공적으로 주문 테이블을 조회하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            setup();

            // when
            MockHttpServletResponse response = mockMvc.perform(get("/api/tables")
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }

        private void setup() {
            final List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable(), new OrderTable());
            Mockito.when(tableRestController.list()).thenReturn(ResponseEntity.ok(orderTables));
        }
    }

    @Nested
    @DisplayName("PUT /api/tables/{orderTableId}/empty")
    public class SetEmpty {
        @Test
        @DisplayName("성공적으로 주문 테이블을 빈 테이블로 표시하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final OrderTable orderTable = setupSuccess();
            final OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(false);

            // when
            MockHttpServletResponse response = mockMvc
                    .perform(put("/api/tables/" + orderTable.getId() + "/empty")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newOrderTable))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }

        private OrderTable setupSuccess() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setEmpty(false);
            Mockito.when(tableRestController.changeEmpty(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ResponseEntity.ok(orderTable));
            return orderTable;
        }

        @Test
        @DisplayName("성공적으로 주문 테이블을 빈 테이블로 표시하면 200 상태 코드를 응답받는다")
        public void badRequest() throws Exception {
            // given
            final OrderTable orderTable = setupBadRequest();
            final OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(false);

            // when
            MockHttpServletResponse response = mockMvc
                    .perform(put("/api/tables/" + orderTable.getId() + "/empty")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newOrderTable))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private OrderTable setupBadRequest() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setEmpty(false);
            Mockito.when(tableRestController.changeEmpty(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ResponseEntity.badRequest().build());
            return orderTable;
        }
    }

    @Nested
    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests")
    public class SetNumberOfGuests {
        private final int NUMBER_OF_GUESTS_TO_CHANGE = 5;

        @Test
        @DisplayName("성공적으로 주문 테이블에 앉은 손님의 숫자를 변경하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final OrderTable orderTable = setupSuccess();
            final OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(NUMBER_OF_GUESTS_TO_CHANGE);

            // when
            MockHttpServletResponse response = mockMvc
                    .perform(put("/api/tables/" + orderTable.getId() + "/number-of-guests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newOrderTable))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final OrderTable orderTableResponse =
                    objectMapper.readValue(response.getContentAsString(), OrderTable.class);
            assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(newOrderTable.getNumberOfGuests());
        }

        private OrderTable setupSuccess() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setNumberOfGuests(NUMBER_OF_GUESTS_TO_CHANGE);
            Mockito.when(tableRestController.changeNumberOfGuests(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ResponseEntity.ok(orderTable));
            return orderTable;
        }

        @Test
        @DisplayName("성공적으로 주문 테이블에 앉은 손님의 숫자를 변경하면 200 상태 코드를 응답받는다")
        public void badRequest() throws Exception {
            // given
            final OrderTable orderTable = setupBadRequest();
            final OrderTable newOrderTable = new OrderTable();
            newOrderTable.setNumberOfGuests(NUMBER_OF_GUESTS_TO_CHANGE);

            // when
            MockHttpServletResponse response = mockMvc
                    .perform(put("/api/tables/" + orderTable.getId() + "/number-of-guests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newOrderTable))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private OrderTable setupBadRequest() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setNumberOfGuests(NUMBER_OF_GUESTS_TO_CHANGE);
            Mockito.when(tableRestController.changeNumberOfGuests(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ResponseEntity.badRequest().build());
            return orderTable;
        }
    }
}
