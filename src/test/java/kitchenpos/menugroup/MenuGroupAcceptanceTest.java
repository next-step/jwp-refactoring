package kitchenpos.menugroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menu.MenuRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private MenuGroup 옛날통닭파세트;

    @BeforeEach
    public void setUp() {
        super.setUp();
        옛날통닭파세트 = MenuGroupRestAssured.from("옛날통닭파세트");
    }

    @DisplayName("메뉴그룹 생성 요청")
    @Test
    void createMenuGroup() {
        MenuGroupRestAssured.메뉴그룹_생성됨(MenuGroupRestAssured.메뉴그룹_생성_요청(옛날통닭파세트));
    }

    @DisplayName("메뉴 생성 예외 확인 - 이름 null")
    @Test
    void makeCreateMenuGroupException_nameIsNull() {
        MenuGroup menuGroup = MenuGroupRestAssured.from(null);
        MenuGroupRestAssured.메뉴그룹_생성_안됨(MenuGroupRestAssured.메뉴그룹_생성_요청(menuGroup));
    }

    @DisplayName("메뉴그룹 생성 및 조회 확인")
    @TestFactory
    Stream<DynamicTest> createAndShowList() {
        return Stream.of(
                DynamicTest.dynamicTest("메뉴 생성 요청", () -> {
                    MenuGroupRestAssured.메뉴그룹_생성됨(MenuGroupRestAssured.메뉴그룹_생성_요청(옛날통닭파세트));
                }),
                DynamicTest.dynamicTest("기존 메뉴그룹(두마리메뉴) 및 새로 생성한 메뉴(옛날통닭파세트) 조회 확인", () -> {
                    ExtractableResponse<Response> response = MenuGroupRestAssured.메뉴그룹_조회_요청();

                    MenuGroupRestAssured.메뉴그룹_조회_목록_응답됨(response);
                    MenuGroupRestAssured.메뉴그룹_조회_목록_포함됨(response, Arrays.asList(옛날통닭파세트.getName(), "두마리메뉴"));
                })
        );
    }
}
