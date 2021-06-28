package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@MockMvcTestConfig
class TableGroupRestControllerTest {

    private static final String BASE_URL = "/api/table-groups";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("단체 지정 생성 요청 성공")
    @Test
    void createTableGroupRequestSuccess() throws Exception {

        // V2__Insert_default_data.sql 에서 ID가 1 ~ 3인 OrderTable 사용
        TableGroup tableGroup = new TableGroup(Stream.iterate(1, i -> i + 1)
                                                     .limit(3)
                                                     .map(i -> orderTableWithId(Long.valueOf(i)))
                                                     .collect(toList()));

        mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(tableGroup))
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable size가 2 미만")
    @Test
    void createTableGroupRequestFail01() {
        TableGroup tableGroup = new TableGroup(Collections.singletonList(orderTableWithId(1L)));
        postFail(tableGroup);
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable id가 중복")
    @Test
    void createTableGroupRequestFail02() {
        TableGroup tableGroup = new TableGroup(Lists.newArrayList(orderTableWithId(1L), orderTableWithId(1L)));
        postFail(tableGroup);
    }

    @DisplayName("단체 지정 생성 요청 실패 - empty 상태가 아닌 orderTable 인입")
    @Test
    void createTableGroupRequestFail03() {
        TableGroup tableGroup = new TableGroup(Lists.newArrayList(orderTableWithId(1L), orderTableWithId(12L)));
        postFail(tableGroup);
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable 의 tableGroupId는 null이 아니어야 함")
    @Test
    void createTableGroupRequestFail04() {
        TableGroup tableGroup = new TableGroup(Lists.newArrayList(orderTableWithId(1L), orderTableWithId(12L)));
        postFail(tableGroup);
    }

    @DisplayName("단체 지정 해제 요청 성공")
    @Test
    void ungroupRequestSuccess() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + 3))
               .andDo(print())
               .andExpect(status().isNoContent());
    }

    @DisplayName("단체 지정 해제 요청 실패 - 단체 지정된 주문 중 주문 상태가 COOKING, MEAL인 것이 있음")
    @Test
    void ungroupRequestFail() {
        try {
            mockMvc.perform(delete(BASE_URL + "/" + 2))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    private OrderTable orderTableWithId(Long id) {
        return new OrderTable(id, null, 0, false);
    }

    private void postFail(TableGroup tableGroup) {
        try {
            mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(tableGroup))
                                          .contentType(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
}
