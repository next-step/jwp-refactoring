package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
@DisplayName("TableGroupRestController 클래스")
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @Nested
    @DisplayName("POST /api/table-groups")
    class Describe_create {

        @Nested
        @DisplayName("등록할 단체 지정할 주문 테이블 목록이 주어지면")
        class Context_with_table_group {
            TableGroup givenTableGroup = new TableGroup();

            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = new OrderTable();
                orderTable1.setId(1L);
                OrderTable orderTable2 = new OrderTable();
                orderTable2.setId(2L);
                givenTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

                when(tableGroupService.create(any(TableGroup.class)))
                        .thenReturn(givenTableGroup);
            }

            @DisplayName("201 Created 와 생성된 단체 지정 테이블을 응답한다.")
            @Test
            void It_responds_created_with_table_group() throws Exception {
                mockMvc.perform(post("/api/table-groups/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(givenTableGroup)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenTableGroup)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/table-groups/{tableGroupId} 은")
    class Describe_ungroup {

        @Nested
        @DisplayName("등록할 해제할 지정할 단체 지정 테이블 식별자가 주어지면")
        class Context_with_table_group_id {
            final Long givenTableGroupId = 1L;

            @DisplayName("204 No Content를 응답한다.")
            @Test
            void It_responds_no_content() throws Exception {
                mockMvc.perform(delete("/api/table-groups/{tableGroupId}", givenTableGroupId))
                        .andExpect(status().isNoContent());
            }
        }
    }
}
