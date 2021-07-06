package kitchenpos.ui;

import kitchenpos.ApiTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ApiTest {

    @MockBean
    private TableGroupService tableGroupService;
    private TableGroup tableGroup;

    @BeforeEach
    public void setUp() {
        super.setUp();

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
    }

    @Test
    @DisplayName("단체 지정을 생성한다")
    void createTest() throws Exception {

        // given
        when(tableGroupService.create(any())).thenReturn(tableGroup);

        // then
        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tableGroup)))
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName("단체 지정을 해제한다")
    void listTest() throws Exception {

        // given
        doNothing().when(tableGroupService).ungroup(any());

        // then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroup.getId()))
                .andExpect(status().isNoContent())
        ;
    }
}