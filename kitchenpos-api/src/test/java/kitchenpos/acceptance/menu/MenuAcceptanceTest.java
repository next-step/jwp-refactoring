package kitchenpos.acceptance.menu;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptance;
import kitchenpos.acceptance.product.ProductAcceptance;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("메뉴와 메뉴상품 인수 테스트")
public class MenuAcceptanceTest extends MenuAcceptance {

	private ProductResponse 치킨;
	private ProductResponse 피자;
	private MenuGroupResponse menuGroupResponse;
	private List<MenuProductRequest> menuProduct1;
	private List<MenuProductRequest> menuProduct2;

	@BeforeEach
	void initData() {
		치킨 = ProductAcceptance.상품_등록되어_있음("치킨", 16000).as(ProductResponse.class);
		피자 = ProductAcceptance.상품_등록되어_있음("피자", 20000).as(ProductResponse.class);
		menuGroupResponse = MenuGroupAcceptance.메뉴_그룹_등록되어_있음("음식").as(MenuGroupResponse.class);
		menuProduct1 = Arrays.asList(MenuProductRequest.of(치킨.getId(), 2), MenuProductRequest.of(피자.getId(), 1));
		menuProduct2 = Arrays.asList(MenuProductRequest.of(치킨.getId(), 2), MenuProductRequest.of(피자.getId(), 2));
	}

	@DisplayName("메뉴를 생성한다.")
	@Test
	void createMenuTest() {
		// given
		MenuRequest request = MenuRequest.of("치피세트", 50000, menuGroupResponse.getId(), menuProduct1);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(request);

		// then
		메뉴_등록됨(response);
	}

	@DisplayName("메뉴 생성에 실패한다.(0보다 작은 금액 or 메뉴가격이 상품들 가격의 합보다 클 경우)")
	@ParameterizedTest
	@ValueSource(longs = {-50000, 55000})
	void createErrorMenuTest(long price) {
		// given
		MenuRequest request = MenuRequest.of("치피세트", price, menuGroupResponse.getId(), menuProduct1);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(request);

		// then
		메뉴_등록_실패됨(response);
	}

	@DisplayName("모든 메뉴를 조회한다.")
	@Test
	void selectMenusTest() {
		// given
		MenuRequest request1 = MenuRequest.of("치치피세트", 50000, menuGroupResponse.getId(), menuProduct1);
		MenuRequest request2 = MenuRequest.of("치치피피세트", 65000, menuGroupResponse.getId(), menuProduct2);
		ExtractableResponse<Response> createResponse1 = 메뉴_등록_요청(request1);
		ExtractableResponse<Response> createResponse2 = 메뉴_등록_요청(request2);

		// when
		ExtractableResponse<Response> response = 메뉴_조회_요청();

		// then
		메뉴_목록_조회됨(response);
		메뉴_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
	}
}
