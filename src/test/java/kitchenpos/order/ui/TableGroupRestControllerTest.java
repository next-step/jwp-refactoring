package kitchenpos.order.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.ui.TableGroupRestController;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        테이블1 = new OrderTable();
        테이블1.setId(4L);
        테이블1.setTableGroupId(101L);

        테이블2 = new OrderTable();
        테이블2.setId(5L);
        테이블2.setTableGroupId(101L);

        테이블_손님이_존재 = new OrderTable();
        테이블_손님이_존재.setId(99L);

        테이블_단체테이블에_속해있는 = new OrderTable();
        테이블_손님이_존재.setId(98L);


        테이블그룹 = new TableGroup();
        테이블그룹.setId(101L);
        테이블그룹.setOrderTables(Arrays.asList(테이블1, 테이블2));


    }

    @Test
    @DisplayName("단체 테이블을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("빈테이블 일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_isEmpty() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList());
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("1개의 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_counting_is_one() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("동일 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_same_orderTables() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1, 테이블1));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("빈테이블이 아닐경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_orderTable_is_not_empty() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1, 테이블_손님이_존재));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("이미 단체테이블에 포함되어있는 테이블일경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_has_group_id() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1, 테이블_단체테이블에_속해있는));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup() throws Exception {
        //when && then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 테이블그룹.getId()))
                .andExpect(status().isNoContent());
    }


    //TODO re ---------

    @Test
    @DisplayName("단체 테이블을 생성한다.")
    void create_re() throws Exception {
        //given

        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        mockMvc.perform(post("/api/table-groups_re")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("빈테이블 일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_isEmpty_re() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList());
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("1개의 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_counting_is_one_re() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("동일 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_same_orderTables_re() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1, 테이블1));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("빈테이블이 아닐경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_orderTable_is_not_empty_re() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1, 테이블_손님이_존재));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("이미 단체테이블에 포함되어있는 테이블일경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_has_group_id_re() throws JsonProcessingException {
        //given
        테이블그룹.setOrderTables(Arrays.asList(테이블1, 테이블_단체테이블에_속해있는));
        String requestBody = objectMapper.writeValueAsString(테이블그룹);

        //when && then
        try {
            mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup_re() throws Exception {
        //when && then
        mockMvc.perform(delete("/api/table-groups_re/{tableGroupId}", 99))
                .andExpect(status().isNoContent());
    }
}