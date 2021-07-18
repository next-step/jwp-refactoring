package kitchenpos.order.ui;

import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.ControllerTest;
import kitchenpos.tablegroup.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;


import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = TableGroupRestController.class)
public class TableGroupControllerTest extends ControllerTest<TableGroupRequest> {

    private static final String BASE_URI = "/api/table-groups";

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @Override
    protected Object controller() {
        return tableGroupRestController;
    }

    private TableGroup 테이블그룹 = new TableGroup(1L);

    @DisplayName("단체지정 생성요청")
    @Test
    void 단체지정_생성요청() throws Exception {
        //Given
        Mockito.when(tableGroupService.create(ArgumentMatchers.any())).thenReturn(new TableGroupResponse(테이블그룹.getId(), 테이블그룹.getCreatedDate()));

        //When
        ResultActions 결과 = postRequest(BASE_URI, new TableGroupRequest(테이블그룹.getId(), 테이블그룹.getCreatedDate()));

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
