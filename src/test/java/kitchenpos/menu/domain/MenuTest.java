package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.domain.MenuGroupTest.두마리메뉴;
import static kitchenpos.menu.domain.MenuGroupTest.한마리메뉴;
import static kitchenpos.menu.domain.MenuProductTest.양념치킨;
import static kitchenpos.menu.domain.MenuProductTest.후라이드;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuTest {
    public static final Menu 치킨세트 = new Menu(
            1L
            , "치킨 세트"
            , new Price(BigDecimal.valueOf(30000))
            , 두마리메뉴
            , Arrays.asList(후라이드, 양념치킨));

    public static final Menu 양념치킨_단품 = new Menu(
            2L
            , "양념치킨 단품"
            , new Price(BigDecimal.valueOf(16000))
            , 한마리메뉴
            , Arrays.asList(양념치킨));

    @Test
    @DisplayName("메뉴 생성")
    void create() {
        // given
        // then
        Menu actual = new Menu(1L
                , "치킨 세트"
                , new Price(BigDecimal.valueOf(30000))
                , 두마리메뉴
                , Arrays.asList(후라이드, 양념치킨));
        // when
        assertThat(actual).isEqualTo(치킨세트);
    }

    @Test
    @DisplayName("메뉴 상품 등록")
    void addMenuProductTest() {
        // given
        Menu 치킨_두마리_세트 = new Menu(1L
                , "치킨 두마리 세트"
                , new Price(BigDecimal.valueOf(30000))
                , 두마리메뉴
                , Arrays.asList(후라이드));
        // then
        치킨_두마리_세트.addMenuProduct(양념치킨);
        List<MenuProduct> actual = 치킨_두마리_세트.getMenuProducts();
        // when
        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactly(후라이드, 양념치킨);
    }
}
