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

    private Menu 양념족발;
    private Menu 튀김족발;

    private MenuProduct 양념;
    private MenuProduct 튀김;
    private MenuProduct 족발;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양념 = new MenuProduct();
        양념.setProductId(1L);
        양념.setQuantity(3);
        족발 = new MenuProduct();
        족발.setProductId(2L);
        족발.setQuantity(2);
        튀김 = new MenuProduct();
        튀김.setProductId(4L);
        튀김.setQuantity(3);

        양념족발 = MenuRestAssured.from("양념족발", 16000, 1L, Arrays.asList(양념, 족발));
    }

    @DisplayName("메뉴 생성 요청")
    @Test
    void createMenu() {
        MenuRestAssured.메뉴_생성됨(MenuRestAssured.메뉴_생성_요청(양념족발));
    }

    @DisplayName("메뉴 생성 확인 - 금액 0원")
    @Test
    void createMenu_priceIsZero() {
        양념족발.setPrice(new BigDecimal(0));
        MenuRestAssured.메뉴_생성됨(MenuRestAssured.메뉴_생성_요청(양념족발));
    }

    @DisplayName("메뉴 생성 예외 확인 - 금액 음수")
    @Test
    void makeCreateMenuException_priceIsMinus() {
        양념족발.setPrice(new BigDecimal(-1));
        MenuRestAssured.메뉴_생성_안됨(MenuRestAssured.메뉴_생성_요청(양념족발));
    }

    @DisplayName("메뉴 생성 및 조회 확인")
    @TestFactory
    Stream<DynamicTest> createAndShowList() {
        return Stream.of(
                DynamicTest.dynamicTest("메뉴 생성 요청", () -> {
                    MenuRestAssured.메뉴_생성됨(MenuRestAssured.메뉴_생성_요청(양념족발));
                    튀김족발 = MenuRestAssured.from("튀김족발", 20000, 1L, Arrays.asList(튀김, 족발));
                    MenuRestAssured.메뉴_생성됨(MenuRestAssured.메뉴_생성_요청(튀김족발));
                }),
                DynamicTest.dynamicTest("새로 생성한 메뉴 존재 조회 확인", () -> {
                    ExtractableResponse<Response> response = MenuRestAssured.메뉴_조회_요청();

                    MenuRestAssured.메뉴_조회_목록_응답됨(response);
                    MenuRestAssured.메뉴_조회_목록_포함됨(response, Arrays.asList(양념족발.getName(), 튀김족발.getName()));
                })
        );
    }
}
