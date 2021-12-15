package kitchenpos.domain;

import static kitchenpos.domain.MenuAcceptanceStaticTest.*;
import static kitchenpos.domain.MenuGroupAcceptanceStaticTest.*;
import static kitchenpos.domain.ProductAcceptanceStaticTest.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

@DisplayName("인수테스트 : 메뉴 관련")
class MenuAcceptanceTest extends AcceptanceTest {

	private MenuGroup 두마리_메뉴_그룹;
	private List<MenuProduct> 불닭_두마리_메뉴_상품_리스트;
	private Product 존재하지_않는_상품;

	@BeforeEach
	void setup() {
		두마리_메뉴_그룹 = 메뉴_그룹_생성되어_있음(메뉴_그룹_생성_요청값_생성("두마리메뉴"));
		Product 불닭 = 상품이_생성_되어있음(상품_요청값_생성("불닭", 16000));
		불닭_두마리_메뉴_상품_리스트 = 메뉴_상품_생성되어_있음(불닭);
		존재하지_않는_상품 = new Product();
		존재하지_않는_상품.setId(100L);
	}

	@Test
	void 메뉴를_생성한다() {
		// give
		Menu 불닭_두마리_메뉴_생성_요청값 = 메뉴_생성_요청값_생성("불닭+불닭", 19000, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트);

		// when
		ExtractableResponse<Response> response = 메뉴_생성_요청(불닭_두마리_메뉴_생성_요청값);

		// then
		메뉴_생성됨(response);
	}

	@Test
	void 메뉴_가격을_넣지_않은_경우_생성_실패한다() {
		// given
		Menu 가격이_없는_불닭_두마리_메뉴_생성_요청값 = 메뉴_생성_요청값_생성("불닭+불닭", null, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트);

		// when
		ExtractableResponse<Response> response = 메뉴_생성_요청(가격이_없는_불닭_두마리_메뉴_생성_요청값);

		// then
		메뉴_생성_실패됨(response);
	}

	@Test
	void 메뉴_가격이_0보다_작은_경우_생성_실패한다() {
		// given
		Menu 가격이_0보다_작은_불닭_두마리_메뉴_생성_요청값 = 메뉴_생성_요청값_생성("불닭+불닭", -1500, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트);

		// when
		ExtractableResponse<Response> response = 메뉴_생성_요청(가격이_0보다_작은_불닭_두마리_메뉴_생성_요청값);

		// then
		메뉴_생성_실패됨(response);
	}

	@Test
	void 메뉴_그룹이_존재하지_않을_경우_생성_실패한다() {
		// given
		Menu 메뉴_그룹이_존재하지_않는_메뉴_생성_요청값 = 메뉴_생성_요청값_생성("불닭+불닭", 19000, 100L, 불닭_두마리_메뉴_상품_리스트);

		// when
		ExtractableResponse<Response> response = 메뉴_생성_요청(메뉴_그룹이_존재하지_않는_메뉴_생성_요청값);

		// then
		메뉴_생성_실패됨(response);
	}

	@Test
	void 메뉴에_등록할_상품이_존재하지_않을_경우_생성_실패한다() {
		// given
		List<MenuProduct> 메뉴_상품_리스트 = 메뉴_상품_생성되어_있음(존재하지_않는_상품);
		Menu 존재하지_않는_상품_메뉴_생성_요청값 = 메뉴_생성_요청값_생성("불닭+불닭", 19000, 10L, 메뉴_상품_리스트);

		// when
		ExtractableResponse<Response> response = 메뉴_생성_요청(존재하지_않는_상품_메뉴_생성_요청값);

		// then
		메뉴_생성_실패됨(response);
	}

	@Test
	void 메뉴의_가격이_메뉴_상품들의_가격을_더한_값보다_크면_생성_실패한다() {
		// given
		Menu 불닭_두마리_메뉴_생성_요청값 = 메뉴_생성_요청값_생성("불닭+불닭", 40000, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트);

		// when
		ExtractableResponse<Response> response = 메뉴_생성_요청(불닭_두마리_메뉴_생성_요청값);

		// then
		메뉴_생성_실패됨(response);
	}

	@Test
	void 메뉴_목록을_조회한다() {
		// given
		Menu 불닭_메뉴 = 메뉴가_생성_되어있음(메뉴_생성_요청값_생성("불닭+불닭", 19000, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트));

		// when
		ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

		// then
		메뉴_목록_조회됨(response, Collections.singletonList(불닭_메뉴.getId()));
	}

}
