package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.domain.OrderLineItemBag;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객이_존재하는_테이블;
import static kitchenpos.order.domain.OrderTableTest.빈_상태;
import static kitchenpos.order.domain.OrderTableTest.빈_테이블;
import static kitchenpos.order.domain.OrderTableTest.한_명의_방문객;
import static kitchenpos.order.domain.OrderTest.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("주문 테이블 ui 테스트")
class TableRestControllerTest {

    private static final OrderStatus 빈_테이블_변경이_가능한_주문_완료_상태 = OrderStatus.COMPLETION;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //given:
        final OrderTable 주문_테이블 = 빈_테이블();
        //when:
        final OrderTable 저장된_주문_테이블 = mapper.readValue(
                mockMvc.perform(post("/api/tables")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(주문_테이블)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), OrderTable.class);
        //then:
        assertThat(저장된_주문_테이블).isEqualTo(주문_테이블);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() throws Exception {
        //when:
        final List<OrderTable> 주문_테이블_목록 = Arrays.asList(mapper.readValue(
                mockMvc.perform(get("/api/tables")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), OrderTable[].class));
        //then:
        assertThat(주문_테이블_목록).isNotEmpty();
    }

    @DisplayName("빈 주문 테이블 변경 성공")
    @Test
    void 빈_주문_테이블_변경_성공() throws Exception {
        //given:
        final OrderTable 저장된_주문_테이블 = tableService.create(두_명의_방문객이_존재하는_테이블());

        orderRepository.save(주문(
                저장된_주문_테이블,
                빈_테이블_변경이_가능한_주문_완료_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Collections.emptyList())));

        저장된_주문_테이블.changeEmpty(빈_상태);
        //when:
        final OrderTable 빈_주문_테이블 = mapper.readValue(
                mockMvc.perform(put("/api/tables/{orderTableId}/empty", 저장된_주문_테이블.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(저장된_주문_테이블)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), OrderTable.class);
        //then:
        assertThat(빈_주문_테이블.isEmpty()).isTrue();
    }

    @DisplayName("방문 손님 수 변경 성공")
    @Test
    void 방문_손님_수_변경_성공() throws Exception {
        //given:
        final OrderTable 저장된_주문_테이블 = tableService.create(두_명의_방문객이_존재하는_테이블());

        저장된_주문_테이블.changeNumberOfGuest(한_명의_방문객);
        //when:
        final OrderTable 빈_주문_테이블 = mapper.readValue(
                mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 저장된_주문_테이블.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(저장된_주문_테이블)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), OrderTable.class);
        //then:
        assertThat(빈_주문_테이블.getNumberOfGuests()).isEqualTo(한_명의_방문객);
    }
}
