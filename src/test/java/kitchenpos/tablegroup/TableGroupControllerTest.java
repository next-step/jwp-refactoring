package kitchenpos.tablegroup;

import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.common.ControllerTest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.ui.TableGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = TableGroupRestController.class)
public class TableGroupControllerTest extends ControllerTest<TableGroup> {

    private static final String BASE_URI = "/api/table-groups";

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRestController tableGroupRestController;

    private TableGroup 테이블그룹;

    @Override
    protected Object controller() {
        return tableGroupRestController;
    }

    @BeforeEach
    void 사전준비() {
        테이블그룹 = new TableGroup();
        테이블그룹.setId(1L);
    }

    @DisplayName("단체지정 생성요청")
    @Test
    void 단체지정_생성요청() throws Exception {
        //Given
        when(tableGroupService.create(any())).thenReturn(테이블그룹);

        //When
        ResultActions 결과 = postRequest(BASE_URI, 테이블그룹);

        //Then
        생성성공(결과);
    }

    @DisplayName("단체지정 취소요청")
    @Test
    void 단체지정_취소요청() throws Exception {
        //When
        ResultActions 결과 = deleteRequest(BASE_URI + "/" + 테이블그룹.getId());

        //Then
        삭제성공(결과);
    }
}
