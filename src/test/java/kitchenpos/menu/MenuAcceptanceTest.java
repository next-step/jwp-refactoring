package kitchenpos.menu;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.menugroup.MenuGroupAcceptanceTest.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.product.ProductAcceptanceTest.*;
import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menugroup.dto.MenuGroupDto;
import kitchenpos.product.dto.ProductDto;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

	private static ExtractableResponse<Response> 메뉴_등록_요청(MenuCreateRequest request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/api/menus")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 메뉴_등록되어_있음(MenuCreateRequest request) {
		return 메뉴_등록_요청(request);
	}

	@DisplayName("메뉴를 등록한다.")
	@Test
	void register() {
		// given
		ProductDto 후라이드_치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드_치킨_상품.getId()));

		// then
		메뉴_등록됨(response);
	}

	@DisplayName("메뉴 이름이 없는 경우 메뉴 등록에 실패한다.")
	@Test
	void registerFailOnEmptyName() {
		// given
		ProductDto 후라이드_치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(new MenuCreateRequest(
			null,
			BigDecimal.valueOf(19000L),
			추천_메뉴_그룹.getId(),
			Collections.singletonList(
				new MenuProductDto(null, null, 후라이드_치킨_상품.getId(), 2)
			)));

		// then
		메뉴_등록되지_않음(response);
	}

	@DisplayName("메뉴 가격이 음수일 경우 메뉴 등록에 실패한다.")
	@Test
	void registerFailOnNegativePrice() {
		// given
		ProductDto 후라이드_치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(new MenuCreateRequest(
			"후라이드+후라이드",
			BigDecimal.valueOf(-19000L),
			추천_메뉴_그룹.getId(),
			Collections.singletonList(
				new MenuProductDto(null, null, 후라이드_치킨_상품.getId(), 2)
			)));

		// then
		메뉴_등록되지_않음(response);
	}

	@DisplayName("메뉴 그룹이 없는 경우 메뉴 등록에 실패한다.")
	@Test
	void registerFailOnEmptyMenuGroup() {
		// given
		ProductDto 후라이드_치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품()).as(ProductDto.class);
		Long unknownMenuGroupId = 0L;

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(new MenuCreateRequest(
			"후라이드+후라이드",
			BigDecimal.valueOf(19000L),
			unknownMenuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, null, 후라이드_치킨_상품.getId(), 2)
			)));

		// then
		메뉴_등록되지_않음(response);
	}

	@DisplayName("등록하려는 상품이 등록되어 있지 않을 경우 메뉴 등록에 실패한다.")
	@Test
	void registerFailOnNotFoundProduct() {
		// given
		Long unknownProductId = 0L;
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(new MenuCreateRequest(
			"후라이드+후라이드",
			BigDecimal.valueOf(19000L),
			추천_메뉴_그룹.getId(),
			Collections.singletonList(
				new MenuProductDto(null, null, unknownProductId, 2)
			)));

		// then
		메뉴_등록되지_않음(response);
	}

	@DisplayName("메뉴의 가격이 메뉴에 속한 상품들의 (가격 * 수량) 합을 넘는 경우 메뉴 등록에 실패한다.")
	@Test
	void registerFailOnPriceInvalid() {
		// given
		ProductDto 후라이드_치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(new MenuCreateRequest(
			"후라이드+후라이드",
			BigDecimal.valueOf(100000L),
			추천_메뉴_그룹.getId(),
			Collections.singletonList(
				new MenuProductDto(null, null, 후라이드_치킨_상품.getId(), 2)
			)));

		// then
		메뉴_등록되지_않음(response);
	}

	@DisplayName("메뉴 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		ProductDto 후라이드치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품()).as(ProductDto.class);
		ProductDto 양념치킨_상품 = 상품_등록되어_있음(양념치킨_상품()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuDto 후라이드후라이드_메뉴 = 메뉴_등록되어_있음(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId())).as(MenuDto.class);
		MenuDto 양념양념_메뉴 = 메뉴_등록되어_있음(양념양념_메뉴(추천_메뉴_그룹.getId(), 양념치킨_상품.getId())).as(MenuDto.class);

		// when
		ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

		// then
		메뉴_목록_응답됨(response);
		메뉴_목록_포함됨(response, Arrays.asList(후라이드후라이드_메뉴, 양념양념_메뉴));
	}

	private void 메뉴_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
		assertThat(response.as(MenuDto.class).getId()).isNotNull();
	}

	private void 메뉴_등록되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/api/menus")
			.then().log().all()
			.extract();
	}

	private void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<MenuDto> expectedMenus) {
		List<Long> actualIds = response.jsonPath().getList(".", MenuDto.class).stream()
			.map(MenuDto::getId)
			.collect(Collectors.toList());

		List<Long> expectedIds = expectedMenus.stream()
			.map(MenuDto::getId)
			.collect(Collectors.toList());

		assertThat(actualIds).isEqualTo(expectedIds);
	}
}
