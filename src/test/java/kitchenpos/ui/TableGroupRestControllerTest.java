package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    private OrderTable orderTable1 = new OrderTable();
    private OrderTable orderTable2 = new OrderTable();
    private TableGroup tableGroup = new TableGroup();

    @BeforeEach
    void setUp() {
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
    }

    @Test
    @DisplayName("단체지정 생성 확인")
    public void whenPostTableGroup_ReturnSatus() throws Exception {
        when(tableGroupService.create(any())).thenReturn(tableGroup);

        mockMvc.perform(post("/api/table-groups")
                .content(asJsonString(tableGroup))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("생성된 단체지정 삭제")
    public void givenTableGroup_whenDeleteTableGroup_ReturnStatus() throws Exception{
        orderTable1.setTableGroupId(null);
        orderTable2.setTableGroupId(null);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        mockMvc.perform(delete("/api/table-groups/{tableGroupId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
