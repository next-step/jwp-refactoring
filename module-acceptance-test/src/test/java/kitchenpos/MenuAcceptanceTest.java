package kitchenpos;

import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.MenuAcceptanceUtil.*;
import static kitchenpos.MenuGroupAcceptanceTestUtil.메뉴_그룹_등록됨;
import static kitchenpos.ProductAcceptanceTestUtil.상품_등록됨;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 신메뉴;
    private ProductResponse 강정치킨;
    private ProductResponse 후라이드;

    @DisplayName("메뉴 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> menu() {
        return Stream.of(
                dynamicTest("메뉴을 등록한다.", () -> {
                    신메뉴 = 메뉴_그룹_등록됨("신메뉴");
                    강정치킨 = 상품_등록됨("강정치킨", BigDecimal.valueOf(15_000L));
                    후라이드 = 상품_등록됨("후라이드", BigDecimal.valueOf(15_000L));

                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정1,후라이드1치킨",
                                                                           BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId(), 강정치킨, 후라이드);

                    메뉴_생성됨(response);
                }),
                dynamicTest("가격이 0미만의 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(-1),
                                                                           신메뉴.getId(), 강정치킨);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("이름이 없는 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청(null, BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId(), 강정치킨);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("메뉴 그룹 없이 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(15_000L),
                                                                           null, 강정치킨);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("상품 없이 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId());

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("존재하지 않는 상품이 포함된 메뉴을 등록한다.", () -> {
                    ProductResponse 존재하지_않는_상품 = new ProductResponse(Long.MAX_VALUE, "존재하지 않는 상품", BigDecimal.TEN);

                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId(), 존재하지_않는_상품);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("상품 가격보다 비싼 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("비싼 강정치킨", BigDecimal.valueOf(18_000L),
                                                                           신메뉴.getId(), 강정치킨);

                    메뉴_생성_실패됨(response);
                }),

                dynamicTest("메뉴 목록을 조회한다.", () -> {
                    ResponseEntity<List<MenuResponse>> response = 메뉴_목록_조회_요청();

                    메뉴_목록_응답됨(response);
                    메뉴_목록_확인됨(response, "강정1,후라이드1치킨");
                    메뉴_목록_메뉴에_메뉴_상품이_포함됨(response, 강정치킨, 후라이드);
                })
        );
    }
}
