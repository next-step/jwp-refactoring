package kitchenpos.menugroup;

import static kitchenpos.menugroup.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupDto;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

	private static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupCreateRequest request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/api/menu-groups")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(MenuGroupCreateRequest request) {
		return 메뉴_그룹_등록_요청(request);
	}

	@DisplayName("메뉴 그룹을 등록한다.")
	@Test
	void register() {
		// when
		ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(추천_메뉴_그룹_요청());

		// then
		메뉴_그룹_등록됨(response);
	}

	@DisplayName("메뉴 그룹 이름이 없는 경우 메뉴 그룹 등록에 실패한다.")
	@Test
	void registerFailOnEmptyName() {
		// when
		ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(이름없는_메뉴_그룹_요청());

		// then
		메뉴_그룹_등록되지_않음(response);
	}

	@DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuGroupDto 비추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(비추천_메뉴_그룹_요청()).as(MenuGroupDto.class);

		// when
		ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

		// then
		메뉴_그룹_목록_응답됨(response);
		메뉴_그룹_목록_포함됨(response, Arrays.asList(추천_메뉴_그룹, 비추천_메뉴_그룹));
	}

	private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
		assertThat(response.as(MenuGroupDto.class).getId()).isNotNull();
	}

	private void 메뉴_그룹_등록되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/api/menu-groups")
			.then().log().all()
			.extract();
	}

	private void 메뉴_그룹_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 메뉴_그룹_목록_포함됨(ExtractableResponse<Response> response, List<MenuGroupDto> expectedMenuGroups) {
		List<Long> actualIds = response.jsonPath().getList(".", MenuGroupDto.class).stream()
			.map(MenuGroupDto::getId)
			.collect(Collectors.toList());

		List<Long> expectedIds = expectedMenuGroups.stream()
			.map(MenuGroupDto::getId)
			.collect(Collectors.toList());

		assertThat(actualIds).isEqualTo(expectedIds);
	}
}
