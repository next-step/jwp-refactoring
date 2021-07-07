package kitchenpos.ui;

import static kitchenpos.domain.OrderTableTest.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

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
import kitchenpos.dto.TableGroupRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@DisplayName("테이블 그룹 통합 테스트")
class TableGroupRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("테이블 그룹을 생성한다")
    void create() throws Exception {
        OrderTableRequest orderTable1 = new OrderTableRequest(테이블3.getId(), 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(테이블4.getId(), 0, true);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(orderTable1, orderTable2));
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.orderTables").isNotEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("특정 테이블 그룹을 삭제한다")
    void ungroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{id}", 2L))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
