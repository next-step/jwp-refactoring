package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestUtils.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestUtils.메뉴_그룹_등록되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTestUtils.상품_등록되어_있음;
import static kitchenpos.domain.MenuProductTestFixture.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@DisplayName("메뉴 인수 테스트")
class MenuAcceptanceTest extends AcceptanceTest {
    private ProductResponse 짜장면;
    private ProductResponse 짬뽕;
    private MenuGroupResponse 면류;
    private MenuProduct 짜장면_1그릇;

    @BeforeEach
    public void setUp() {
        super.setUp();

        짜장면 = 상품_등록되어_있음("짜장면", BigDecimal.valueOf(7000));
        짬뽕 = 상품_등록되어_있음("짬뽕", BigDecimal.valueOf(8000));
        면류 = 메뉴_그룹_등록되어_있음("면류");
        짜장면_1그릇 = menuProduct(1L, null, 짜장면.getId(), 1);
    }

    @DisplayName("메뉴 인수 테스트")
    @TestFactory
    Stream<DynamicNode> menuAcceptance() {
        return Stream.of(
                dynamicTest("가격이 없는 메뉴를 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청("짜장1", 면류.getId(), null,
                            Collections.singletonList(짜장면_1그릇));

                    // then
                    메뉴_생성_실패(response);
                }),
                dynamicTest("가격이 0원 이하인 메뉴를 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청("짜장1", 면류.getId(), BigDecimal.valueOf(-2000),
                            Collections.singletonList(짜장면_1그릇));

                    // then
                    메뉴_생성_실패(response);
                }),
                dynamicTest("메뉴 그룹에 속해있지 않은 메뉴를 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청("짜장1", 면류.getId(), BigDecimal.valueOf(-2000),
                            Collections.singletonList(짜장면_1그릇));

                    // then
                    메뉴_생성_실패(response);
                }),
                dynamicTest("상품으로 등록되어 있지 않은 메뉴를 등록한다.", () -> {
                    // given
                    MenuProduct 우동_1그릇 = menuProduct(2L, null, 100L, 1);

                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청("우동1", 면류.getId(), BigDecimal.valueOf(9000),
                            Collections.singletonList(우동_1그릇));

                    // then
                    메뉴_생성_실패(response);
                }),
                dynamicTest("메뉴 가격은 메뉴 상품가격의 합계보다 클 수 없다.", () -> {
                    // given
                    MenuProduct 짬뽕_2그릇 = menuProduct(2L, null, 짬뽕.getId(), 2);

                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청("짜장1_짬뽕2", 면류.getId(), BigDecimal.valueOf(25000),
                            Arrays.asList(짜장면_1그릇, 짬뽕_2그릇));

                    // then
                    메뉴_생성_실패(response);
                }),
                dynamicTest("메뉴를 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_생성_요청("짜장1", 면류.getId(), BigDecimal.valueOf(7000),
                            Collections.singletonList(짜장면_1그릇));

                    // then
                    메뉴_생성됨(response);
                }),
                dynamicTest("메뉴 목록을 조회하면 메뉴 목록이 반환된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

                    // then
                    메뉴_목록_조회됨(response);
                    메뉴_목록_포함됨(response, "짜장1");
                })
        );
    }
}
