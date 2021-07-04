package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.RestControllerTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("단체 지정 API")
@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest extends RestControllerTest<TableGroup> {

    private static final String BASE_URL = "/api/table-groups";

    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup 단체지정 = new TableGroup(1L, LocalDateTime.now(), new ArrayList<>());

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(tableGroupService.create(any())).willReturn(단체지정);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(단체지정);
        post(BASE_URL, 단체지정)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() throws Exception {
        // When & Then
        delete(BASE_URL + "/" + 단체지정.getId())
            .andExpect(status().isNoContent());
    }

}
