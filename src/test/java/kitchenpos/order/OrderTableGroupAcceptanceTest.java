package kitchenpos.order;

import static kitchenpos.order.OrderTableAcceptanceTest.*;
import static kitchenpos.order.OrderTableFixture.*;
import static kitchenpos.order.OrderTableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderTableDto;
import kitchenpos.order.dto.OrderTableGroupCreateRequest;
import kitchenpos.order.dto.OrderTableGroupDto;

@DisplayName("주문 테이블 그룹 인수 테스트")
public class OrderTableGroupAcceptanceTest extends AcceptanceTest {

	private static ExtractableResponse<Response> 주문_테이블_그룹_등록_요청(OrderTableGroupCreateRequest request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 주문_테이블_그룹_등록되어_있음(OrderTableGroupCreateRequest request) {
		return 주문_테이블_그룹_등록_요청(request);
	}

	@DisplayName("주문 테이블 그룹을 등록한다.")
	@Test
	void register() {
		// given
		OrderTableDto 빈_주문_테이블_1 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableDto 빈_주문_테이블_2 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_그룹_등록_요청(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId())));

		// then
		주문_테이블_그룹_등록됨(response);
	}

	@DisplayName("주문 테이블 갯수가 2개 미만이면 등록할 수 없다.")
	@Test
	void registerFailOnLessThanTwo() {
		// given
		OrderTableDto 빈_주문_테이블_1 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_그룹_등록_요청(
			주문_테이블_그룹(Collections.singletonList(빈_주문_테이블_1.getId())));

		// then
		주문_테이블_그룹_등록되지_않음(response);
	}

	@DisplayName("주문 테이블이 등록되어 있지 않은 경우 등록할 수 없다.")
	@Test
	void registerFailOnNotFoundOrderTable() {
		// given
		OrderTableDto 빈_주문_테이블_1 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		Long unknownOrderTableId = 0L;

		// when
		ExtractableResponse<Response> response = 주문_테이블_그룹_등록_요청(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), unknownOrderTableId)));

		// then
		주문_테이블_그룹_등록되지_않음(response);
	}

	@DisplayName("주문 테이블이 비어있지 않은 경우 등록할 수 없다.")
	@Test
	void registerFailOnNotEmptyOrderTable() {
		// given
		OrderTableDto 빈_주문_테이블 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블()).as(OrderTableDto.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_그룹_등록_요청(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블.getId(), 비어있지않은_주문_테이블.getId())));

		// then
		주문_테이블_그룹_등록되지_않음(response);
	}

	@DisplayName("이미 주문 테이블 그룹에 등록된 주문 테이블이 있는 경우 등록할 수 없다.")
	@Test
	void registerFailOnAlreadyBelongToOrderTableGroup() {
		// given
		OrderTableDto 빈_주문_테이블_1 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableDto 빈_주문_테이블_2 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableDto 빈_주문_테이블_3 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		주문_테이블_그룹_등록되어_있음(주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId())));

		// when
		ExtractableResponse<Response> response = 주문_테이블_그룹_등록_요청(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_3.getId())));

		// then
		주문_테이블_그룹_등록되지_않음(response);
	}

	@DisplayName("주문 테이블 그룹을 해제할 수 있다.")
	@Test
	void ungroup() {
		// given
		OrderTableDto 빈_주문_테이블_1 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableDto 빈_주문_테이블_2 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableGroupDto 주문_테이블_그룹 = 주문_테이블_그룹_등록되어_있음(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId()))).as(OrderTableGroupDto.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_그룹_해제_요청(주문_테이블_그룹.getId());

		// then
		주문_테이블_그룹_해제됨(response);
	}

	@DisplayName("주문 테이블 그룹에 속한 주문 테이블들 중 완료되지 않은 주문이 있는 경우 주문 테이블 그룹을 해제할 수 없다.")
	@Test
	void ungroupFailOnNotCompletedOrderExist() {
		// TODO
	}

	private void 주문_테이블_그룹_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
		assertThat(response.as(OrderTableGroupDto.class).getId()).isNotNull();
	}

	private void 주문_테이블_그룹_등록되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private ExtractableResponse<Response> 주문_테이블_그룹_해제_요청(Long orderTableGroupId) {
		return RestAssured
			.given().log().all()
			.when().delete("/api/table-groups/{orderTableGroupId}", orderTableGroupId)
			.then().log().all()
			.extract();
	}

	private void 주문_테이블_그룹_해제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
