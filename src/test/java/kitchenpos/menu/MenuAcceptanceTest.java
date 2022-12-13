package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

public class MenuAcceptanceTest extends AcceptanceTest {

    private Menu 양념치킨;
    private Menu 간장치킨;

    private MenuProduct 양념;
    private MenuProduct 간장;
    private MenuProduct 치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양념 = new MenuProduct();
        양념.setProductId(1L);
        양념.setQuantity(3);
        치킨 = new MenuProduct();
        치킨.setProductId(2L);
        치킨.setQuantity(2);
        간장 = new MenuProduct();
        간장.setProductId(4L);
        간장.setQuantity(3);

        양념치킨 = MenuRestAssured.from("양념치킨", 16000, 1L, Arrays.asList(양념, 치킨));
    }

    @DisplayName("메뉴 생성 요청")
    @Test
    void createMenu() {
        MenuRestAssured.메뉴_생성됨(MenuRestAssured.메뉴_생성_요청(양념치킨));
    }

    @DisplayName("메뉴 생성 확인 - 금액 0원")
    @Test
    void createMenu_priceIsZero() {
        양념치킨.setPrice(new BigDecimal(0));
        MenuRestAssured.메뉴_생성됨(MenuRestAssured.메뉴_생성_요청(양념치킨));
    }

    @DisplayName("메뉴 생성 예외 확인 - 금액 음수")
    @Test
    void makeCreateMenuException_priceIsMinus() {
        양념치킨.setPrice(new BigDecimal(-1));
        MenuRestAssured.메뉴_생성_안됨(MenuRestAssured.메뉴_생성_요청(양념치킨));
    }

    @DisplayName("메뉴 생성 및 조회 확인")
    @TestFactory
    Stream<DynamicTest> createAndShowList() {
        return Stream.of(
                DynamicTest.dynamicTest("메뉴 생성 요청", () -> {
                    간장치킨 = MenuRestAssured.from("간장치킨", 20000, 1L, Arrays.asList(간장, 치킨));

                    MenuRestAssured.메뉴_생성됨(MenuRestAssured.메뉴_생성_요청(간장치킨));
                }),
                DynamicTest.dynamicTest("기존 메뉴 및 새로 생성한 메뉴까지 조회 확인", () -> {
                    ExtractableResponse<Response> response = MenuRestAssured.메뉴_조회_요청();

                    MenuRestAssured.메뉴_조회_목록_응답됨(response);
                    MenuRestAssured.메뉴_조회_목록_포함됨(response, Arrays.asList(양념치킨.getName(), 간장치킨.getName()));
                })
        );
    }
}
