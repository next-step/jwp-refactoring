package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    @DisplayName("주문 등록")
    @Test
    void create() {
        // given
       Menu menu = 메뉴_등록_되어짐().as(Menu.class);
       OrderTable orderTable = 주문_테이블_등록_되어짐().as(OrderTable.class);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderTable.getId());

        List<Map<String, Object>> orderLineItems = new ArrayList<>();
        Map<String, Object> orderLineItem = new HashMap<>();
        orderLineItem.put("menuId", menu.getId());
        orderLineItem.put("quantity", 1);
        orderLineItems.add(orderLineItem);
        params.put("orderLineItems", orderLineItems);

        // when
        ExtractableResponse<Response> response = 주문_등록_요청(params);

        // then
        주문_둥록됨(response);
    }

    private ExtractableResponse<Response> 메뉴_등록_되어짐() {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1L));
        menuProducts.add(new MenuProduct(2L, 2L, 1L));
        menuProducts.add(new MenuProduct(3L, 3L, 1L));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드치킨");
        params.put("price", 16000);
        params.put("menuGroupId", 2);
        params.put("menuProducts", menuProducts);

        return MenuAcceptanceTest.메뉴_등록_요청(params);
    }

    private ExtractableResponse<Response> 주문_테이블_등록_되어짐() {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", 0);
        params.put("empty", false);

        // when
        return OrderTableAcceptanceTest.주문_테이블_등록_요청(params);
    }


    private ExtractableResponse<Response> 주문_등록_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all().extract();
    }

    private void 주문_둥록됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


}
