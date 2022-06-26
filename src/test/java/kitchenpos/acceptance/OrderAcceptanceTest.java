package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.acceptance.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String ORDER_URL = "/v2/api/orders/";
    private ProductResponse 짬뽕;
    private MenuGroupResponse 중식;
    private MenuResponse 중식_메뉴;
    private OrderTableResponse 테이블;

    @TestFactory
    Stream<DynamicTest> orderTest() {
        return Stream.of(
                dynamicTest("상품, 메뉴그룹, 메뉴, 테이블 준비", () -> {
                    짬뽕 = 상품_생성_요청("짬뽕", BigDecimal.valueOf(10000L)).as(ProductResponse.class);
                    중식 = 메뉴그룹_생성_요청("중식").as(MenuGroupResponse.class);
                    테이블 = 테이블_생성_요청(3, false).as(OrderTableResponse.class);

                    중식_메뉴 = 메뉴_생성_요청("중식_메뉴", 1000L, 중식.getId(),
                            Arrays.asList(MenuProductRequest.of(짬뽕.getId(), 1L))).as(MenuResponse.class);
                }),
                dynamicTest("주문 생성", () -> {
                    ExtractableResponse<Response> 주문_생성_요청 = 주문_생성_요청(테이블.getId(),
                            Lists.newArrayList(OrderLineItemRequest.of(중식_메뉴.getId(), 1L)));
                    주문_생성됨(주문_생성_요청);
                })
                );
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long
                                                                 orderTableId,
                                                         List<OrderLineItemRequest> orderLineItems) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(OrderRequest.of(orderTableId, orderLineItems))
                .when().post(ORDER_URL)
                .then().log().all().
                extract();
    }

}
