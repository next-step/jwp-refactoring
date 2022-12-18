package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableGroupTestFixture.단체_지정_생성됨;
import static kitchenpos.table.acceptance.TableGroupTestFixture.단체_지정_요청;
import static kitchenpos.table.acceptance.TableGroupTestFixture.단체_지정_해제_요청;
import static kitchenpos.table.acceptance.TableGroupTestFixture.단체_지정_해제됨;
import static kitchenpos.table.acceptance.TableTestFixture.주문_테이블_생성_요청함;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private TableGroupRequest 테이블그룹;


    @BeforeEach
    public void setUp() {
        super.setUp();
        OrderTableResponse 빈테이블일번 = 주문_테이블_생성_요청함(new OrderTableRequest(0, true)).as(OrderTableResponse.class);
        OrderTableResponse 빈테이블이번 = 주문_테이블_생성_요청함(new OrderTableRequest(0, true)).as(OrderTableResponse.class);
        테이블그룹 = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(빈테이블일번.getId()), new OrderTableIdRequest(
                빈테이블이번.getId())));
    }

    @DisplayName("단체를 지정한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 단체_지정_요청(테이블그룹);
        //then
        단체_지정_생성됨(response);
    }

    @DisplayName("단체를 지정을 해제한다.")
    @Test
    void unGroup() {
        //given
        TableGroupResponse savedTableGroup = 단체_지정_요청(테이블그룹).as(TableGroupResponse.class);
        //when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(savedTableGroup.getId());
        //then
        단체_지정_해제됨(response);
    }
}
