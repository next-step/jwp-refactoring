package kitchenpos.ui;

import static kitchenpos.domain.OrderTableTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.utils.IntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("테이블 그룹 통합 테스트")
class TableGroupRestControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    OrderTableRequest 비어있는_테이블_1;
    OrderTableRequest 비어있는_테이블_2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        비어있는_테이블_1 = new OrderTableRequest(테이블3.getId(), 0, true);
        비어있는_테이블_2 = new OrderTableRequest(테이블4.getId(), 0, true);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다")
    void create() throws Exception {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(비어있는_테이블_1, 비어있는_테이블_2));
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderTables").isNotEmpty())
            .andExpect(jsonPath("$.orderTables[0].id").value(테이블3.getId()))
            .andExpect(jsonPath("$.orderTables[0].numberOfGuests").value(테이블3.getNumberOfGuests().value()))
            .andExpect(jsonPath("$.orderTables[0].empty").value(false));
    }

    @Test
    @DisplayName("테이블 그룹 생성에 실패한다 - 테이블 갯수가 2개 미만일 경우")
    void create_failed_1() throws Exception {
        TableGroupRequest request = new TableGroupRequest(Collections.singletonList(비어있는_테이블_1));
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("테이블 그룹 생성에 실패한다 - 주문 테이블이 존재하지 않을 경우")
    void create_failed_2() throws Exception {
        Long invalidId = -1L;
        비어있는_테이블_1.setId(invalidId);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(비어있는_테이블_1, 비어있는_테이블_2));
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("테이블 그룹 생성에 실패한다 - 이미 그룹에 포함된 경우")
    void create_failed_3() throws Exception {
        Long 이미_그룹에_포함된_테이블_ID = 1L;
        비어있는_테이블_1.setId(이미_그룹에_포함된_테이블_ID);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(비어있는_테이블_1, 비어있는_테이블_2));
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("테이블 그룹 생성에 실패한다 - 비어있지 않은 경우")
    void create_failed_4() throws Exception {
        Long 비어있지_않은_테이블_ID = 11L;
        비어있는_테이블_1.setId(비어있지_않은_테이블_ID);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(비어있는_테이블_1, 비어있는_테이블_2));
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("특정 테이블 그룹을 삭제한다")
    void ungroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{id}", 1L))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("테이블 그룹 삭제가 실패한다 - 식사중인 테이블 존재")
    void ungroup_failed() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{id}", 2L))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
