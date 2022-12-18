package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceUtils.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceUtils.*;
import static kitchenpos.acceptance.ProductAcceptanceUtils.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends AcceptanceTest {

	private MenuGroup 한마리메뉴;
	private Product 간장치킨;

	@BeforeEach
	public void setup() {
		super.setUp();
		한마리메뉴 = 메뉴_그룹_등록_되어_있음("한마리메뉴");
		간장치킨 = 상품_등록_되어_있음("간장치킨", BigDecimal.valueOf(16000));
	}

	/**
	 * given 메뉴 그룹, 상품, 가격을 정하고
	 * when 메뉴 등록을 요청하면
	 * then 메뉴가 등록된다
	 */
	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void createMenuTest() {
		// given
		String name = "후라이드치킨";
		BigDecimal price = BigDecimal.valueOf(16000);
		int quantity = 1;

		// when
		ExtractableResponse<Response> 메뉴_등록_요청 = 메뉴_등록_요청(name, price, 한마리메뉴.getId(), 간장치킨.getId(), quantity);

		// then
		메뉴_등록_됨(메뉴_등록_요청, name, price, quantity, 한마리메뉴, 간장치킨);
	}

	/**
	 * given 메뉴가 등록되어 있고
	 * when 메뉴 목록을 조회하면
	 * then 목록이 조회된다.
	 */
	@DisplayName("메뉴 목록을 조회할 수 있다.")
	@Test
	void menuListTest() {
		// given
		String name = "후라이드치킨";
		BigDecimal price = BigDecimal.valueOf(16000);
		int quantity = 1;
		Menu 후라이드치킨 = 메뉴_등록_되어_있음(name, price, 한마리메뉴.getId(), 간장치킨.getId(), quantity);

		// when
		ExtractableResponse<Response> 메뉴_목록_조회_요청 = 메뉴_목록_조회_요청();

		// then
		메뉴_목록_조회_됨(메뉴_목록_조회_요청, 후라이드치킨, 한마리메뉴);
	}
}
