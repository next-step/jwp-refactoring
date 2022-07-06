package kitchenpos.utils.generator;

import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_구성_상품_생성;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성_요청_생성;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuProductRequest;
import org.junit.jupiter.api.BeforeEach;

public class ScenarioTestFixtureGenerator {

    protected static Product 물냉면, 비빔냉면, 삼겹살, 항정살, 고추장_불고기;
    protected static MenuGroup 고기랑_같이, 고기만_듬뿍;
    protected static Menu 커플_냉삼_메뉴, 고기_더블_더블_메뉴;
    protected static List<MenuProduct> 커플_냉삼_메뉴_구성_상품, 고기_더블_더블_메뉴_구성_상품;

    protected static CreateMenuRequest 커플_냉삼_메뉴_생성_요청, 고기_더블_더블_메뉴_생성_요청;
    protected static List<MenuProductRequest> 커플_냉삼_메뉴_구성_상품_생성_요청, 고기_더블_더블_메뉴_구성_상품_생성_요청;

    @BeforeEach
    public void setUp() {
        커플_냉삼_메뉴_상품_생성();
        커플_냉삼_메뉴_생성();
        커플_냉삼_메뉴_생성_요청_생성();

        고기_더블_더블_메뉴_상품_생성();
        고기_더블_더블_메뉴_생성();
        고기_더블_더블_메뉴_생성_요청_생성();
    }

    protected static void 커플_냉삼_메뉴_상품_생성() {
        물냉면 = 상품_생성("물냉면", 8_000);
        비빔냉면 = 상품_생성("비빔냉면", 8_000);
        삼겹살 = 상품_생성("삼겹살", 15_000);
    }

    protected static void 커플_냉삼_메뉴_생성() {
        고기랑_같이 = 메뉴_그룹_생성("고기랑 같이");
        커플_냉삼_메뉴 = 메뉴_생성(
            "커플_냉삼",
            25_000,
            고기랑_같이,
            메뉴_구성_상품_생성(물냉면, 1),
            메뉴_구성_상품_생성(비빔냉면, 1),
            메뉴_구성_상품_생성(삼겹살, 1)
        );
        커플_냉삼_메뉴_구성_상품 = 커플_냉삼_메뉴.getMenuProducts();
    }

    protected static void 커플_냉삼_메뉴_생성_요청_생성() {
        커플_냉삼_메뉴_생성_요청 = 메뉴_생성_요청_생성(
            "커플_냉삼",
            25_000,
            고기랑_같이,
            메뉴_구성_상품_생성(물냉면, 1),
            메뉴_구성_상품_생성(비빔냉면, 1),
            메뉴_구성_상품_생성(삼겹살, 1)
        );
        커플_냉삼_메뉴_구성_상품_생성_요청 = 커플_냉삼_메뉴_생성_요청.getMenuProductRequests();
    }

    protected static void 고기_더블_더블_메뉴_상품_생성() {
        항정살 = 상품_생성("항정살", 20_000);
        고추장_불고기 = 상품_생성("고추장_불고기", 15_000);
    }

    protected static void 고기_더블_더블_메뉴_생성() {
        고기만_듬뿍 = 메뉴_그룹_생성("고기만_듬뿍");
        고기_더블_더블_메뉴 = 메뉴_생성(
            "고기_더블_더블",
            30_000,
            고기만_듬뿍,
            메뉴_구성_상품_생성(항정살, 1),
            메뉴_구성_상품_생성(고추장_불고기, 1)
        );
        고기_더블_더블_메뉴_구성_상품 = 고기_더블_더블_메뉴.getMenuProducts();
    }

    protected static void 고기_더블_더블_메뉴_생성_요청_생성() {
        고기_더블_더블_메뉴_생성_요청 = 메뉴_생성_요청_생성(
            "고기_더블_더블",
            30_000,
            고기만_듬뿍,
            메뉴_구성_상품_생성(항정살, 1),
            메뉴_구성_상품_생성(고추장_불고기, 1)
        );
        고기_더블_더블_메뉴_구성_상품_생성_요청 = 커플_냉삼_메뉴_생성_요청.getMenuProductRequests();
    }
}
