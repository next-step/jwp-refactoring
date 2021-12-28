package kitchenpos.tablegroup.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTestHelper.*;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestHelper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("단체 좌석 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    OrderTable 좌석1;
    OrderTable 좌석2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        좌석1 = 좌석_등록_되어_있음(true, 0, null).as(OrderTable.class);
        좌석2 = 좌석_등록_되어_있음(true, 0, null).as(OrderTable.class);
    }

    @DisplayName("단체 좌석 생성")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> 단체_좌석_생성_요청_응답 = 단체_좌석_생성_요청(좌석1, 좌석2);

        // then
        단쳬_좌석_생성됨(단체_좌석_생성_요청_응답);
    }

    @DisplayName("단체 좌석 해제")
    @Test
    void ungroupTableGroup() {
        // given
        ExtractableResponse<Response> 단체_좌석_생성_요청_응답 = 단체_좌석_생성_요청(좌석1, 좌석2);
        Long 단체_좌석_아이디 = 단체_좌석_생성_요청_응답.as(TableGroup.class).getId();

        // when
        ExtractableResponse<Response> 단체_좌석_해제_요청_응답 = 단체_좌석_해제_요청(단체_좌석_아이디);

        // then
        단쳬_좌석_해제됨(단체_좌석_해제_요청_응답);
    }
}
