package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.RestAssuredUtils;

public class TableGroupAcceptStep {

	public static final String BASE_URL = "/api/table-groups";

	public static void 단체_삭제_확인(ExtractableResponse<Response> 단체_삭제_응답) {
		assertThat(단체_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static ExtractableResponse<Response> 단체_삭제_요청(ExtractableResponse<Response> 단체_생성_응답) {
		String url = 단체_생성_응답.header("Location");
		return RestAssuredUtils.delete(url);
	}

	public static TableGroup 단체_생성_확인(ExtractableResponse<Response> 단체_생성_응답, TableGroup 단체_생성_요청_데이터) {
		TableGroup 생성된_단체 = 단체_생성_응답.as(TableGroup.class);
		assertAll(
			() -> assertThat(단체_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> 단체에_생성된_테이블_확인(생성된_단체, 단체_생성_요청_데이터)
		);
		return 생성된_단체;
	}

	public static void 단체에_생성된_테이블_확인(TableGroup 생성된_단체, TableGroup 단체_생성_요청_데이터) {
		List<Long> 등록된_테이블 = 생성된_단체.getOrderTables()
			.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		List<Long> 요청한_테이블 = 단체_생성_요청_데이터.getOrderTables()
			.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		assertThat(등록된_테이블).containsExactlyElementsOf(요청한_테이블);
	}

	public static ExtractableResponse<Response> 단체_생성_요청(TableGroup 단체_생성_요청_데이터) {
		return RestAssuredUtils.post(BASE_URL, 단체_생성_요청_데이터);
	}
}
