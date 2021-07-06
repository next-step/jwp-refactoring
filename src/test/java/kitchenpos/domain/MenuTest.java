package kitchenpos.domain;

import static kitchenpos.domain.MenuGroupTest.*;
import static kitchenpos.domain.MenuProductTest.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.ExceedingTotalPriceException;

@DisplayName("메뉴 단위 테스트")
public class MenuTest {
    public static Menu 후라이드_메뉴 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴);
    public static Menu 양념치킨_메뉴 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴);
    public static Menu 반반치킨_메뉴 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴);
    public static Menu 통구이_메뉴 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴);
    public static Menu 간장치킨_메뉴 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴);
    public static Menu 순살치킨_메뉴 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴);

    static {
        후라이드_메뉴.addMenuProduct(MP1);
        양념치킨_메뉴.addMenuProduct(MP2);
        반반치킨_메뉴.addMenuProduct(MP3);
        통구이_메뉴.addMenuProduct(MP4);
        간장치킨_메뉴.addMenuProduct(MP5);
        순살치킨_메뉴.addMenuProduct(MP6);
    }

    @Test
    @DisplayName("메뉴 가격이 단품 가격의 합을 초과")
    void create_fail4() {
        assertThatThrownBy(() -> 후라이드_메뉴.withPrice(BigDecimal.valueOf(50000)))
            .isInstanceOf(ExceedingTotalPriceException.class);
    }

    @Test
    @DisplayName("메뉴 생성 실패(가격없음)")
    void create_fail1() {
        assertThatThrownBy(() -> 순살치킨_메뉴.withPrice(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
