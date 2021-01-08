package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderTableService;
import kitchenpos.ui.dto.orderTable.OrderTableRequest;
import kitchenpos.ui.dto.orderTable.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
                .willReturn(new OrderTableResponse(orderTableId, tableGroupId, numberOfGuests, empty));

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + orderTableId));
    }
}