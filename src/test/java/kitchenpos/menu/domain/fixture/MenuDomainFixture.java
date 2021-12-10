package kitchenpos.menu.domain.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.utils.AcceptanceTest;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.fixture.MenuGroupDomainFixture.일인_세트;
import static kitchenpos.utils.AcceptanceFixture.get;
import static kitchenpos.utils.AcceptanceFixture.post;

public class MenuDomainFixture extends AcceptanceTest {

    public static Menu 후라이드_치킨 = menu("후라이드 치킨", BigDecimal.valueOf(15000), 일인_세트);


    public static Menu menu(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return post("/api/menus", menuRequest);
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return get("/api/menus");
    }

}
