package kitchenpos.ordertable.ui;

import static kitchenpos.ordertable.domain.OrderTableTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.utils.IntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("주문 테이블 통합 테스트")
class TableRestControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void create() throws Exception {
        OrderTableRequest request = new OrderTableRequest(6, false);
        mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.numberOfGuests").value(request.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(request.isEmpty()));
    }

    @Test
    @DisplayName("주문 테이블 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/tables"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[0].id").value(테이블1.getId()))
            .andExpect(jsonPath("$[0].numberOfGuests").value(테이블1.getNumberOfGuests().value()))
            .andExpect(jsonPath("$[0].empty").value(테이블1.isEmpty()));
    }

    @Test
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
    @DisplayName("상태 변경 실패 - 주문 테이블이 존재하지 않을 경우")
    void changeEmpty_failed_1() throws Exception {
        Long invalidId = -1L;
        OrderTableRequest request = new OrderTableRequest(10, false);
        mockMvc.perform(put("/api/tables/{id}/empty", invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상태 변경 실패 - 테이블 그룹에 포함되어 있을 경우")
    void changeEmpty_failed_2() throws Exception {
        OrderTableRequest request = new OrderTableRequest(10, false);
        mockMvc.perform(put("/api/tables/{id}/empty", 테이블1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("상태 변경 실패 - 테이블의 주문이 조리/식사중일 경우")
    void changeEmpty_failed_3() throws Exception {
        OrderTableRequest request = new OrderTableRequest(10, false);
        mockMvc.perform(put("/api/tables/{id}/empty", 테이블9_사용중.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
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

    @Test
    @DisplayName("손님 수 변경 실패 - 손님 수가 0보다 작을 경우")
    void changeNumberOfGuests_failed_1() throws Exception {
        int invalidNumberOfGuests = -1;
        OrderTableRequest request = new OrderTableRequest(invalidNumberOfGuests, false);
        mockMvc.perform(put("/api/tables/{id}/number-of-guests", 테이블9_사용중.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("손님 수 변경 실패 - 주문 테이블이 존재하지 않을 경우")
    void changeNumberOfGuests_failed_2() throws Exception {
        Long invalidId = -1L;
        OrderTableRequest request = new OrderTableRequest(10, false);
        mockMvc.perform(put("/api/tables/{id}/empty", invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("손님 수 변경 실패 - 주문 테이블이 비어있을 경우")
    void changeNumberOfGuests_failed_3() throws Exception {
        OrderTableRequest request = new OrderTableRequest(10, false);
        mockMvc.perform(put("/api/tables/{id}/number-of-guests", 테이블3.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
    }
}
