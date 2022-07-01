package kitchenpos.fixture.acceptance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.acceptance.MenuAcceptanceTest;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuResponse;

public class AcceptanceTestMenuFixture {
    public final AcceptanceTestMenuGroupFixture 메뉴_그룹;
    public final AcceptanceTestProductFixture 상품;

    public final MenuResponse 돼지모듬;
    public final MenuResponse 김치찌개정식;

    public AcceptanceTestMenuFixture() {
        메뉴_그룹 = new AcceptanceTestMenuGroupFixture();
        상품 = new AcceptanceTestProductFixture();

        final Map<Product, Long> 돼지모듬_상품_수량 = new HashMap<>();
        돼지모듬_상품_수량.put(상품.삼겹살, 2L);
        돼지모듬_상품_수량.put(상품.목살, 1L);
        돼지모듬 = MenuAcceptanceTest.메뉴_생성_요청(
                "돼지모듬", BigDecimal.valueOf(43000L), 메뉴_그룹.구이류, 돼지모듬_상품_수량).as(MenuResponse.class);

        final Map<Product, Long> 김치찌개정식_상품_수량 = new HashMap<>();
        김치찌개정식_상품_수량.put(상품.김치찌개, 1L);
        김치찌개정식_상품_수량.put(상품.공깃밥, 1L);
        김치찌개정식 = MenuAcceptanceTest.메뉴_생성_요청(
                "김치찌개정식", BigDecimal.valueOf(9000L), 메뉴_그룹.식사류, 김치찌개정식_상품_수량).as(MenuResponse.class);
    }
}
