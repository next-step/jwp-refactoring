package kitchenpos.product.acceptance;

import static kitchenpos.product.acceptance.ProductAcceptanceTestUtils.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@DisplayName("상품 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {
    @DisplayName("상품 인수 테스트")
    @TestFactory
    Stream<DynamicNode> productAcceptance() {
        return Stream.of(
                dynamicTest("가격이 0보다 작은 상품을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 상품_생성_요청("짜장면", BigDecimal.valueOf(-1000));

                    // then
                    상품_생성_실패(response);
                }),
                dynamicTest("가격이 없는 상품을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 상품_생성_요청("짜장면", null);

                    // then
                    상품_생성_실패(response);
                }),
                dynamicTest("상품을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 상품_생성_요청("짜장면", BigDecimal.valueOf(8000));

                    // then
                    상품_생성됨(response);
                }),
                dynamicTest("상품 목록을 조회하면 상품 목록이 반환된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 상품_목록_조회_요청();

                    // then
                    상품_목록_조회됨(response);
                    상품_목록_포함됨(response, "짜장면");
                })
        );
    }
}
