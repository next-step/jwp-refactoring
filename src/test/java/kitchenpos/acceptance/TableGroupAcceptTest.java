package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.TableGroupAcceptStep.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.step.TableAcceptStep;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("단체 인수테스트")
public class TableGroupAcceptTest extends AcceptanceTest {

	@DisplayName("단체를 관리한다")
	@Test
	void 단체를_관리한다() {
		// given
		OrderTable 테이블1번 = TableAcceptStep.테이블_등록_되어_있음(2, true);
		OrderTable 테이블2번 = TableAcceptStep.테이블_등록_되어_있음(4, true);

		TableGroup 단체_생성_요청_데이터 = new TableGroup();
		단체_생성_요청_데이터.setOrderTables(Arrays.asList(테이블1번, 테이블2번));

		// when
		ExtractableResponse<Response> 단체_생성_응답 = 단체_생성_요청(단체_생성_요청_데이터);

		// then
		단체_생성_확인(단체_생성_응답, 단체_생성_요청_데이터);

		// when
		ExtractableResponse<Response> 단체_삭제_응답 = 단체_삭제_요청(단체_생성_응답);

		// then
		단체_삭제_확인(단체_삭제_응답);

	}

}
