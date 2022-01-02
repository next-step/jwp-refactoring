package kitchenpos.fixture;

import static kitchenpos.common.AcceptanceFixture.*;
import static kitchenpos.fixture.MenuGroupAcceptanceFixture.*;

import java.math.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;

public class MenuAcceptanceFixture extends AcceptanceTest {

    public static Menu 후라이드_치킨 = menu("후라이드 치킨", BigDecimal.valueOf(15000), 한마리_세트);
    public static MenuRequest 후라이드_치킨_요청 = MenuRequest.of(후라이드_치킨.getName(),
            후라이드_치킨.getPrice(), 1L);

    public static Menu menu(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return post("/api/menus", menuRequest);
    }
}
