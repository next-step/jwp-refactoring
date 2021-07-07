package kitchenpos.ui;

import static kitchenpos.domain.OrderTableTest.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.domain.OrderTableTest;
import kitchenpos.dto.OrderTableRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@DisplayName("주문 테이블 통합 테스트")
class TableRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("주문 테이블을 생성한다")
    void create() throws Exception {
        OrderTableRequest request = new OrderTableRequest(6, false);
        mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.numberOfGuests").value(request.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(request.isEmpty()));
    }

    @Test
    @Order(2)
    @DisplayName("주문 테이블 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/tables"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].numberOfGuests").value(0))
            .andExpect(jsonPath("$[0].empty").value(true));
    }

    @Test
    @Order(3)
    @DisplayName("특정 테이블의 상태를 변경한다")
    void changeEmpty() throws Exception {
        OrderTableRequest request = new OrderTableRequest(10, false);
        mockMvc.perform(put("/api/tables/{id}/empty", 테이블5.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.empty").value(request.isEmpty()));
    }

    @Test
    @Order(4)
    @DisplayName("특정 테이블의 손님 수를 변경한다")
    void changeNumberOfGuests() throws Exception {
        OrderTableRequest request = new OrderTableRequest(10, false);
        mockMvc.perform(put("/api/tables/{id}/number-of-guests", 테이블9_사용중.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.numberOfGuests").value(request.getNumberOfGuests()));
    }
}
