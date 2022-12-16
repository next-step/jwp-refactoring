package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableGroupTestFixture.단체_지정_해제됨;
import static kitchenpos.acceptance.TableGroupTestFixture.단체_지정_생성됨;
import static kitchenpos.acceptance.TableGroupTestFixture.단체_지정_요청;
import static kitchenpos.acceptance.TableGroupTestFixture.단체_지정_해제_요청;
import static kitchenpos.acceptance.TableTestFixture.주문_테이블_생성_요청함;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 빈테이블일번;
    private OrderTable 빈테이블이번;
    private TableGroup 테이블그룹;


    @BeforeEach
    public void setUp() {
        super.setUp();
        빈테이블일번 = 주문_테이블_생성_요청함(new OrderTable(null, null, 0, true)).as(OrderTable.class);
        빈테이블이번 = 주문_테이블_생성_요청함(new OrderTable(null, null, 0, true)).as(OrderTable.class);
        테이블그룹 = new TableGroup(null, null, Arrays.asList(빈테이블일번, 빈테이블이번));
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
        TableGroup savedTableGroup = 단체_지정_요청(테이블그룹).as(TableGroup.class);
        //when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(savedTableGroup.getId());
        //then
        단체_지정_해제됨(response);
    }
}
