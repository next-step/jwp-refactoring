package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.table.dto.CreateTableGroupDto;
import kitchenpos.table.dto.OrderTableDto;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

        // V2__Insert_default_data.sql에서 ID가 1, 2, 3인 order_table 데이터 활용
        CreateTableGroupDto createTableGroupDto =
            new CreateTableGroupDto(Stream.iterate(1, i -> i + 1)
                                          .limit(3)
                                          .map(i -> orderTableWithId(Long.valueOf(i)))
                                          .collect(toList()));

        createTableGroup(createTableGroupDto);
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable size가 2 미만")
    @Test
    void createTableGroupRequestFail01() {
        postFail(new CreateTableGroupDto(Collections.singletonList(orderTableWithId(1L))));
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable id가 중복")
    @Test
    void createTableGroupRequestFail02() {
        postFail(new CreateTableGroupDto(Lists.newArrayList(orderTableWithId(1L), orderTableWithId(1L))));
    }

    @DisplayName("단체 지정 생성 요청 실패 - empty 상태가 아닌 orderTable 인입")
    @Test
    void createTableGroupRequestFail03() {
        postFail(new CreateTableGroupDto(Lists.newArrayList(orderTableWithId(1L), orderTableWithId(12L))));
    }

    @DisplayName("단체 지정 생성 요청 실패 - orderTable 의 tableGroupId는 null이 아니어야 함")
    @Test
    void createTableGroupRequestFail04() {
        postFail(new CreateTableGroupDto(Lists.newArrayList(orderTableWithId(1L), orderTableWithId(12L))));
    }

    @DisplayName("단체 지정 해제 요청 성공")
    @Test
    void ungroupRequestSuccess() throws Exception {

        // V2__Insert_default_data.sql에서 ID가 4, 5, 6 order_table 데이터 활용
        MvcResult result = createTableGroup(new CreateTableGroupDto(Stream.iterate(4, i -> i + 1)
                                                                          .limit(3)
                                                                          .map(i -> orderTableWithId(Long.valueOf(i)))
                                                                          .collect(toList())));

        String location = (String) result.getResponse().getHeaderValue("Location");
        assert location != null;

        mockMvc.perform(delete(location))
               .andDo(print())
               .andExpect(status().isNoContent());
    }

    private MvcResult createTableGroup(CreateTableGroupDto createTableGroupDto) throws Exception {
        return mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(createTableGroupDto))
                                             .contentType(MediaType.APPLICATION_JSON))
                      .andDo(print())
                      .andExpect(status().isCreated())
                      .andExpect(header().exists("Location"))
                      .andReturn();
    }

    private OrderTableDto orderTableWithId(Long id) {
        return new OrderTableDto(id, null, 0, false);
    }

    private void postFail(CreateTableGroupDto createTableGroupDto) {
        try {
            mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(createTableGroupDto))
                                          .contentType(MediaType.APPLICATION_JSON))
                   .andDo(print())
                   .andExpect(status().isBadRequest());
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
}
