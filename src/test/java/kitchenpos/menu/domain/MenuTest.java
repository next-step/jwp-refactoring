package kitchenpos.menu.domain;

import kitchenpos.menu.fixture.MenuProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 테스트")
class MenuTest {
    private MenuProduct 더블강정_메뉴_상품;

    @BeforeEach
    void setup() {
        더블강정_메뉴_상품 = MenuProductFixture.create(1L, 2L);
    }

    @DisplayName("메뉴 생성 확인")
    @Test
    void 메뉴_생성_확인() {
        // given
        String 메뉴_이름 = "더블강정";
        BigDecimal 메뉴_가격 = BigDecimal.valueOf(32_000L);
        Long 메뉴_그룹_ID = 1L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Menu.of(메뉴_이름, 메뉴_가격, 메뉴_그룹_ID, Collections.singletonList(더블강정_메뉴_상품));

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("생성시 메뉴 이름은 반드시 존재")
    @Test
    void 생성시_메뉴_이름은_반드시_존재() {
        // given
        String 메뉴_이름 = null;
        BigDecimal 메뉴_가격 = BigDecimal.valueOf(32_000L);
        Long 메뉴_그룹_ID = 1L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Menu.of(메뉴_이름, 메뉴_가격, 메뉴_그룹_ID, Collections.singletonList(더블강정_메뉴_상품));

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성시 메뉴 가격은 반드시 존재")
    @Test
    void 생성시_메뉴_가격은_반드시_존재() {
        // given
        String 메뉴_이름 = "더블강정";
        BigDecimal 메뉴_가격 = null;
        Long 메뉴_그룹_ID = 1L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Menu.of(메뉴_이름, 메뉴_가격, 메뉴_그룹_ID, Collections.singletonList(더블강정_메뉴_상품));

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성시 메뉴 그룹 ID는 반드시 존재")
    @Test
    void 생성시_메뉴_그룹_ID는_반드시_존재() {
        // given
        String 메뉴_이름 = "더블강정";
        BigDecimal 메뉴_가격 = BigDecimal.valueOf(32_000L);
        Long 메뉴_그룹_ID = null;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Menu.of(메뉴_이름, 메뉴_가격, 메뉴_그룹_ID, Collections.singletonList(더블강정_메뉴_상품));

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성시 메뉴 상품은 반드시 존재")
    @Test
    void 생성시_메뉴_상품은_반드시_존재() {
        // given
        String 메뉴_이름 = "더블강정";
        BigDecimal 메뉴_가격 = BigDecimal.valueOf(32_000L);
        Long 메뉴_그룹_ID = 1L;

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Menu.of(메뉴_이름, 메뉴_가격, 메뉴_그룹_ID, Collections.emptyList());

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
