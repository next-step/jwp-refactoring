package kitchenpos.domain;

import static java.util.Collections.*;
import static kitchenpos.domain.MenuGroupTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.ExceedingTotalPriceException;

@DisplayName("메뉴 단위 테스트")
public class MenuTest {
    public static Menu 후라이드_메뉴 = new Menu(1L, "후라이드치킨", Price.valueOf(16000), 한마리메뉴, MenuProducts.of(new MenuProduct(후라이드, 1L)));
    public static Menu 양념치킨_메뉴 = new Menu(2L, "양념치킨", Price.valueOf(16000), 한마리메뉴, MenuProducts.of(new MenuProduct(양념치킨, 1L)));
    public static Menu 반반치킨_메뉴 = new Menu(3L, "반반치킨", Price.valueOf(16000), 한마리메뉴, MenuProducts.of(new MenuProduct(반반치킨, 1L)));
    public static Menu 통구이_메뉴 = new Menu(4L, "통구이", Price.valueOf(16000), 한마리메뉴, MenuProducts.of(new MenuProduct(통구이, 1L)));
    public static Menu 간장치킨_메뉴 = new Menu(5L, "간장치킨", Price.valueOf(16000), 한마리메뉴, MenuProducts.of(new MenuProduct(간장치킨, 1L)));
    public static Menu 순살치킨_메뉴 = new Menu(6L, "순살치킨", Price.valueOf(16000), 한마리메뉴, MenuProducts.of(new MenuProduct(순살치킨, 1L)));

    @Test
    @DisplayName("메뉴 가격이 단품 가격의 합을 초과")
    void create_fail4() {
        Price 오만원 = Price.valueOf(50000);
        List<MenuProduct> 후라이드_하나 = singletonList(new MenuProduct(후라이드, 1L));
        assertThatThrownBy(() -> {
            new Menu("어떤메뉴", 오만원, 한마리메뉴, MenuProducts.of(후라이드_하나));
        })
            .isInstanceOf(ExceedingTotalPriceException.class);
    }

    @Test
    @DisplayName("메뉴 생성 실패(가격없음)")
    void create_fail1() {
        List<MenuProduct> 후라이드_하나 = singletonList(new MenuProduct(후라이드, 1L));
        assertThatThrownBy(() -> new Menu("어떤메뉴", null, 한마리메뉴, MenuProducts.of(후라이드_하나)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
