package kitchenpos.table.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.domain.entity.TableGroup;
import kitchenpos.table.domain.value.NumberOfGuests;
import kitchenpos.table.domain.value.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.service.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@AutoConfigureMockMvc
class TableGroupRestControllerTest {

    MockMvc mockMvc;
    @Autowired
    TableGroupRestController tableGroupRestController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TableGroupService tableGroupService;

    TableGroup 테이블그룹;
    OrderTable 테이블1;
    OrderTable 테이블2;
    OrderTable 테이블_손님이_존재;
    OrderTable 테이블_단체테이블에_속해있는;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
            .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .alwaysDo(print())
            .build();

        테이블1 = new OrderTable(4L, NumberOfGuests.of(4), true);
        테이블2 = new OrderTable(5L, NumberOfGuests.of(4), true);

        테이블_손님이_존재 = new OrderTable();

        테이블_단체테이블에_속해있는 = new OrderTable();

        테이블그룹 = new TableGroup(new OrderTables(Arrays.asList(테이블1, 테이블2)));

    }

    @Test
    @DisplayName("단체 테이블을 생성한다.")
    void create() throws Exception {

        OrderTableRequest 주문테이블_리퀘스트1 = new OrderTableRequest(1L, 99L, 4, true);
        OrderTableRequest 주문테이블_리퀘스트2 = new OrderTableRequest(2L, 99L, 4, true);
        TableGroupRequest 테이블그룹_리퀘스트 = new TableGroupRequest(
            Arrays.asList(주문테이블_리퀘스트1, 주문테이블_리퀘스트2));

        //given
        String requestBody = objectMapper.writeValueAsString(테이블그룹_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("빈테이블 일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_isEmpty() throws Exception {
        //given
        TableGroupRequest 테이블그룹_리퀘스트 = new TableGroupRequest(Arrays.asList());

        String requestBody = objectMapper.writeValueAsString(테이블그룹_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("1개의 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_counting_is_one() throws Exception {
        //given
        OrderTableRequest 주문테이블_리퀘스트1 = new OrderTableRequest(1L, 99L, 4, true);
        TableGroupRequest 테이블그룹_리퀘스트 = new TableGroupRequest(Arrays.asList(주문테이블_리퀘스트1));
        String requestBody = objectMapper.writeValueAsString(테이블그룹_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("동일 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_same_orderTables() throws Exception {
        //given
        OrderTableRequest 주문테이블_리퀘스트1 = new OrderTableRequest(1L, 99L, 4, true);
        TableGroupRequest 테이블그룹_리퀘스트 = new TableGroupRequest(
            Arrays.asList(주문테이블_리퀘스트1, 주문테이블_리퀘스트1));
        String requestBody = objectMapper.writeValueAsString(테이블그룹_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("빈테이블이 아닐경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_orderTable_is_not_empty() throws Exception {
        //given
        OrderTableRequest 주문테이블_리퀘스트1 = new OrderTableRequest(1L, 99L, 4, true);
        OrderTableRequest 주문테이블_리퀘스트2 = new OrderTableRequest(7L, 99L, 4, true);
        TableGroupRequest 테이블그룹_리퀘스트 = new TableGroupRequest(
            Arrays.asList(주문테이블_리퀘스트1, 주문테이블_리퀘스트2));
        String requestBody = objectMapper.writeValueAsString(테이블그룹_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이미 단체테이블에 포함되어있는 테이블일경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_has_group_id() throws Exception {
        //given
        OrderTableRequest 주문테이블_리퀘스트1 = new OrderTableRequest(1L, 99L, 4, true);
        OrderTableRequest 주문테이블_리퀘스트2 = new OrderTableRequest(98L, 99L, 4, true);
        TableGroupRequest 테이블그룹_리퀘스트 = new TableGroupRequest(
            Arrays.asList(주문테이블_리퀘스트1, 주문테이블_리퀘스트2));
        String requestBody = objectMapper.writeValueAsString(테이블그룹_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup() throws Exception {
        //when && then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 99))
            .andExpect(status().isNoContent());
    }
}