package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableGroupAcceptStep.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableResponse;

@DisplayName("단체 인수테스트")
public class TableGroupAcceptTest extends AcceptanceTest {

	@DisplayName("단체를 관리한다")
	@Test
	void 단체를_관리한다() {
		// given
		TableResponse 테이블1번 = TableAcceptStep.테이블_등록_되어_있음(2, false);
		TableResponse 테이블2번 = TableAcceptStep.테이블_등록_되어_있음(4, false);

		List<Long> ids = Stream.of(테이블1번, 테이블2번)
			.map(TableResponse::getId).collect(Collectors.toList());
		TableGroupRequest 단체_생성_요청_데이터 = new TableGroupRequest(ids);

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
