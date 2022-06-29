package kitchenpos;

import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.ProductAcceptanceTestUtil.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> product() {
        return Stream.of(
                dynamicTest("상품을 등록한다.", () -> {
                    ResponseEntity<ProductResponse> response = 상품_생성_요청("강정치킨", BigDecimal.valueOf(15_000L));

                    상품_생성됨(response);
                }),
                dynamicTest("가격이 0미만인 상품을 등록한다.", () -> {
                    ResponseEntity<ProductResponse> response = 상품_생성_요청("강정치킨", BigDecimal.valueOf(-1L));

                    상품_생성_실패됨(response);
                }),
                dynamicTest("이름이 없는 상품을 등록한다.", () -> {
                    ResponseEntity<ProductResponse> response = 상품_생성_요청(null, BigDecimal.valueOf(15_000L));

                    상품_생성_실패됨(response);
                }),
                dynamicTest("상품 목록을 조회한다.", () -> {
                    ResponseEntity<List<ProductResponse>> response = 상품_목록_조회_요청();

                    상품_목록_응답됨(response);
                    상품_목록_확인됨(response, "강정치킨");
                })
        );
    }
}
