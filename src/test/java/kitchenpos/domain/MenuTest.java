package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.PriceTest.MINUS_PRICE;
import static kitchenpos.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 테스트")
public class MenuTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        final String 이름 = "자메이카 통다리 1인 세트";
        final BigDecimal 가격 = BigDecimal.ONE;
        final Long 메뉴_그룹_id = 1L;
        final List<MenuProduct> 메뉴_상품_목록 = Arrays.asList(
                MenuProduct.of(상품("통다리"), 1),
                MenuProduct.of(상품("콜라"), 1)
        );
        assertThat(Menu.of(이름, 가격, 메뉴_그룹_id, 메뉴_상품_목록)).isEqualTo(Menu.of(이름, 가격, 메뉴_그룹_id, 메뉴_상품_목록));
    }

    @DisplayName("생성 예외 - 메뉴의 가격이 0보다 적은 경우")
    @Test
    void 생성_예외_메뉴의_가격이_0보다_적은_경우() {
        final String 이름 = "자메이카 통다리 1인 세트";
        final BigDecimal 가격 = MINUS_PRICE;
        final Long 메뉴_그룹_id = 1L;
        final List<MenuProduct> 메뉴_상품_목록 = Arrays.asList(
                MenuProduct.of(상품("통다리"), 1),
                MenuProduct.of(상품("콜라"), 1)
        );
        assertThatIllegalArgumentException().isThrownBy(() -> Menu.of(이름, 가격, 메뉴_그룹_id, 메뉴_상품_목록));
    }

    public static Menu 메뉴(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }
}
