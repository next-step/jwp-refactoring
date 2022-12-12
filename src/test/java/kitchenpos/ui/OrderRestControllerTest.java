package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class OrderRestControllerTest extends BaseTest {
    private final OrderTable 좌석 = new OrderTable(1L, 1L, 4, false);
    private final List<OrderLineItem> orderLineItems =
            Arrays.asList(new OrderLineItem(1L, 1L, 1L, 1));

    @Test
    void 생성() throws Exception {
        String content = objectMapper.writeValueAsString(좌석);
        TableRestControllerTest.생성_요청(content);

        content = objectMapper.writeValueAsString(
                new Order(좌석.getId(), null, LocalDateTime.now(), orderLineItems));
        생성_요청(content);
    }

    @Test
    void 조회() throws Exception {
        String content = objectMapper.writeValueAsString(좌석);
        TableRestControllerTest.생성_요청(content);

        content = objectMapper.writeValueAsString(
                new Order(좌석.getId(), null, LocalDateTime.now(), orderLineItems));
        생성_요청(content);

        조회_요청();
    }

    @Test
    void 주문_상태_변경() throws Exception {
        String content = objectMapper.writeValueAsString(좌석);
        TableRestControllerTest.생성_요청(content);

        content = objectMapper.writeValueAsString(
                new Order(좌석.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));
        Long id = 생성_요청(content);

        상태_변경_요청(id, content);
    }

    private Long 생성_요청(String content) throws Exception {
        MvcResult response = mockMvc.perform(post("/api/orders")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        return ID_반환(response);
    }

    private Long ID_반환(MvcResult response){
        String location = response.getResponse().getHeader("Location");
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(location);
        matcher.find();
        return Long.parseLong(matcher.group(), 10);
    }

    private void 조회_요청() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private void 상태_변경_요청(Long id, String content) throws Exception {
        mockMvc.perform(put("/api/orders/" + id + "/order-status")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
