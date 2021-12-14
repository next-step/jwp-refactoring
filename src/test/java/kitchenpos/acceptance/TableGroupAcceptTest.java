package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.step.TableAcceptStep;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.acceptance.step.TableGroupAcceptStep.단체_지정_요청;
import static kitchenpos.acceptance.step.TableGroupAcceptStep.단체_지정_확인;
import static kitchenpos.acceptance.step.TableGroupAcceptStep.단체_해지_요청;
import static kitchenpos.acceptance.step.TableGroupAcceptStep.단체_해지_확인;

@DisplayName("단체 인수테스트")
class TableGroupAcceptTest extends AcceptanceTest {
    @DisplayName("단체를 관리한다")
    @Test
    void 단체를_관리한다() {
        // given
        OrderTable 일번_테이블 = TableAcceptStep.테이블이_등록되어_있음(2, true);
        OrderTable 이번_테이블 = TableAcceptStep.테이블이_등록되어_있음(4, true);

        TableGroup 단체_등록_요청_데이터 = new TableGroup();
        단체_등록_요청_데이터.setOrderTables(Arrays.asList(일번_테이블, 이번_테이블));

        // when
        ExtractableResponse<Response> 단체_등록_응답 = 단체_지정_요청(단체_등록_요청_데이터);

        // then
        단체_지정_확인(단체_등록_응답, 단체_등록_요청_데이터);

        // when
        ExtractableResponse<Response> 단체_해지_응답 = 단체_해지_요청(단체_등록_응답);

        /// then
        단체_해지_확인(단체_해지_응답);
    }
}
