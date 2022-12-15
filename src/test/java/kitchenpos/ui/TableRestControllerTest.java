package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

class TableRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문테이블을 등록한다")
    @Test
    void test1() throws Exception {
        Long id = 주문테이블_등록();
        assertThat(orderTableRepository.findById(id)).isNotEmpty();
    }

    @DisplayName("전체 주문테이블을 조회한다")
    @Test
    void test2() throws Exception {
        주문테이블_등록();

        mockMvc.perform(get("/api/tables"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..numberOfGuests").exists())
            .andExpect(jsonPath("$..empty").exists())
        ;
    }

    @DisplayName("빈 테이블 여부를 변경한다")
    @Test
    void test3() throws Exception {
        OrderTableRequest request = new OrderTableRequest(false);
        Long id = 주문테이블_등록();

        mockMvc.perform(put("/api/tables/" + id + "/empty")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.empty").value(request.isEmpty()));

        assertThat(findById(id).isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("방문한 손님수를 변경한다")
    @Test
    void test4() throws Exception {
        OrderTableRequest request = new OrderTableRequest(99);
        Long id = 주문테이블_등록();

        mockMvc.perform(put("/api/tables/" + id + "/number-of-guests")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.numberOfGuests").value(request.getNumberOfGuests()));

        assertThat(findById(id).getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    private OrderTable findById(Long id) {
        return orderTableRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    private Long 주문테이블_등록() throws Exception {
        OrderTableRequest request = new OrderTableRequest(3, false);

        MvcResult result = mockMvc.perform(post("/api/tables")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.numberOfGuests").value(request.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(request.isEmpty()))
            .andReturn();

        return getId(result);
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, OrderTableResponse.class).getId();
    }

}
