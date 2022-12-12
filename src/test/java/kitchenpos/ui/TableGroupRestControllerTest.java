package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TableGroupRestController.class)
@DisplayName("TableGroupRestController 클래스 테스트")
public class TableGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TableGroupRestController tableGroupRestController;

    @Nested
    @DisplayName("POST /api/table-groups")
    public class PostMethod {
        @Test
        @DisplayName("성공적으로 테이블 그룹을 생성하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final TableGroup tableGroup = setupSuccess();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/table-groups")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tableGroup))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final TableGroup tableGroupResponse =
                    objectMapper.readValue(response.getContentAsString(), TableGroup.class);
            assertThat(tableGroupResponse.getOrderTables()).hasSize(tableGroup.getOrderTables().size());
        }

        private TableGroup setupSuccess() {
            final TableGroup tableGroup = new TableGroup();
            final List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());
            tableGroup.setOrderTables(orderTables);
            Mockito.when(tableGroupRestController.create(Mockito.any())).thenReturn(ResponseEntity.ok(tableGroup));
            return tableGroup;
        }

        @Test
        @DisplayName("테이블 그룹을 생성하는데 실패하면 400 상태 코드를 응답받는다")
        public void errorBadRequest() throws Exception {
            // given
            final TableGroup tableGroup = setupErrorBadRequest();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/table-groups")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tableGroup))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private TableGroup setupErrorBadRequest() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(new ArrayList<>());
            Mockito.when(tableGroupRestController.create(Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
            return tableGroup;
        }
    }

    @Nested
    @DisplayName("DELETE /api/table-groups/{tableGroupId}")
    public class DeleteMethod {
        @Test
        @DisplayName("성공적으로 테이블 그룹을 해제하면 204 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final TableGroup tableGroup = setupSuccess();

            // when
            MockHttpServletResponse response = mockMvc
                    .perform(delete("/api/table-groups/" + tableGroup.getId())
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        private TableGroup setupSuccess() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setId(1L);
            final List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());
            tableGroup.setOrderTables(orderTables);
            Mockito.when(tableGroupRestController.ungroup(Mockito.anyLong()))
                    .thenReturn(ResponseEntity.noContent().build());
            return tableGroup;
        }

        @Test
        @DisplayName("테이블 그룹을 해제하는데 실패하면 400 상태 코드를 응답받는다")
        public void errorBadRequest() throws Exception {
            // given
            final TableGroup tableGroup = setupErrorBadRequest();

            // when
            MockHttpServletResponse response = mockMvc
                    .perform(delete("/api/table-groups/" + tableGroup.getId())
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private TableGroup setupErrorBadRequest() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setId(1L);
            final List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());
            tableGroup.setOrderTables(orderTables);
            Mockito.when(tableGroupRestController.ungroup(Mockito.anyLong()))
                    .thenReturn(ResponseEntity.badRequest().build());
            return tableGroup;
        }
    }
}
