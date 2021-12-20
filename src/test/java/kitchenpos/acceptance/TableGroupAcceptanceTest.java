package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.TableAcceptanceStep.주문테이블_생성됨;
import static kitchenpos.acceptance.step.TableGroupAcceptanceStep.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체지정 관리 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {

    OrderTable 주문테이블_1;
    OrderTable 주문테이블_2;
    TableGroup 단체지정;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        주문테이블_1 = 주문테이블_생성됨(OrderTable.of(1, true));
        주문테이블_2 = 주문테이블_생성됨(OrderTable.of(1, true));
        List<OrderTable> 주문테이블목록 = Arrays.asList(주문테이블_1, 주문테이블_2);
        단체지정 = TableGroup.of(주문테이블목록);
    }

    @Test
    @DisplayName("단체지정 관리")
    void 단체지정_관리() {
        // when
        ExtractableResponse<Response> 단체지정_등록_결과 = 단체지정_등록_요청(단체지정);
        // then
        TableGroup 등록된_단체지정 = 단체지정_등록_검증(단체지정_등록_결과, 단체지정);

        // when
        ExtractableResponse<Response> 단체지정_해지_결과 = 단체지정_해지_요청(등록된_단체지정.getId());
        // then
        단체지정_해지_검증(단체지정_해지_결과);
    }
}
