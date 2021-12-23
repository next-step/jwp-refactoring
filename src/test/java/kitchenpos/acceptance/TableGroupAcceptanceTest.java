package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.TableAcceptanceStep.주문테이블_생성됨;
import static kitchenpos.acceptance.step.TableGroupAcceptanceStep.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.order.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체지정 관리 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("단체지정 관리")
    void 단체지정_관리() {
        // given
        TableGroupRequest 단체지정 = 단체지정();

        // when
        ExtractableResponse<Response> 단체지정_등록_결과 = 단체지정_등록_요청(단체지정);
        // then
        Long 등록된_단체지정_번호 = 단체지정_등록_검증(단체지정_등록_결과, 단체지정);

        // when
        ExtractableResponse<Response> 단체지정_해지_결과 = 단체지정_해지_요청(등록된_단체지정_번호);
        // then
        단체지정_해지_검증(단체지정_해지_결과);
    }

    private TableGroupRequest 단체지정() {
        Long 주문테이블_번호_1 = 주문테이블_생성됨(0, true);
        Long 주문테이블_번호_2 = 주문테이블_생성됨(0, true);
        List<Long> 주문테이블목록 = Arrays.asList(주문테이블_번호_1, 주문테이블_번호_2);
        return new TableGroupRequest(주문테이블목록);
    }
}
