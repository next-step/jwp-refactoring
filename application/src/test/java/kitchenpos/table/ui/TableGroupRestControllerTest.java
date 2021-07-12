package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.OrderTableIdRequest;
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

        // V2__Insert_default_data.sql에서 ID가 4, 5, 6인 order_table 데이터 활용
        CreateTableGroupRequest request =
            new CreateTableGroupRequest(Stream.iterate(4, i -> i + 1)
                                              .limit(3)
                                              .map(this::orderTableIdRequest)
                                              .collect(toList()));

        mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(request))
                                      .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"))
               .andReturn();
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable size가 2 미만")
    @Test
    void createTableGroupRequestFail01() {
        postFail(new CreateTableGroupRequest(Collections.singletonList(orderTableIdRequest(1))));
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable id가 중복")
    @Test
    void createTableGroupRequestFail02() {
        postFail(new CreateTableGroupRequest(Lists.newArrayList(orderTableIdRequest(1), orderTableIdRequest(1))));
    }

    @DisplayName("단체 지정 생성 요청 실패 - empty 상태가 아닌 orderTable 인입")
    @Test
    void createTableGroupRequestFail03() {
        postFail(new CreateTableGroupRequest(Lists.newArrayList(orderTableIdRequest(1), orderTableIdRequest(12))));
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable 의 tableGroupId는 null이 아니어야 함")
    @Test
    void createTableGroupRequestFail04() {
        postFail(new CreateTableGroupRequest(Lists.newArrayList(orderTableIdRequest(1), orderTableIdRequest(12))));
    }

    @DisplayName("단체 지정 해제 요청 성공")
    @Test
    void ungroupRequestSuccess() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/3"))
               .andDo(print())
               .andExpect(status().isNoContent());
    }

    private OrderTableIdRequest orderTableIdRequest(Integer id) {
        return orderTableIdRequest(Long.valueOf(id));
    }

    private OrderTableIdRequest orderTableIdRequest(Long id) {
        return new OrderTableIdRequest(id);
    }

    private void postFail(CreateTableGroupRequest request) {
        try {
            mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(request))
                                          .contentType(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
}
