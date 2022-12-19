package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 단위 테스트")
public class MenuTest {
    private MenuGroup 양식;
    private Menu 양식_세트;
    private MenuProduct 스테이크;
    private MenuProduct 스파게티;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", 43000, 양식);

        스테이크 = new MenuProduct(양식_세트, new Product("스테이크", 25000), 1);
        스파게티 = new MenuProduct(양식_세트, new Product("스파게티", 18000), 1);
    }

    @DisplayName("이름, 가격, 메뉴 그룹이 동일하면 메뉴는 동일하다.")
    @Test
    void 이름_가격_메뉴_그룹이_동일하면_메뉴는_동일하다() {
        assertEquals(
                new Menu("양식 세트", 50000, 양식),
                new Menu("양식 세트", 50000, 양식)
        );
    }

    @DisplayName("이름이 null이거나 빈값이면 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_null이거나_빈값이면_메뉴를_생성할_수_없다(String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Menu(name, 43000, 양식))
                .withMessage(ErrorMessage.MENU_REQUIRED_NAME.getMessage());
    }

    @DisplayName("메뉴 그룹이 null이면 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴_그룹이_null이면_메뉴를_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Menu("양식 세트", 43000, null))
                .withMessage(ErrorMessage.MENU_REQUIRED_MENU_GROUP.getMessage());
    }

    @DisplayName("메뉴 가격이 null이면 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴_가격이_null이면_메뉴를_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Menu("양식 세트", null, 양식))
                .withMessage(ErrorMessage.MENU_REQUIRED_PRICE.getMessage());
    }

    @DisplayName("메뉴 가격이 음수면 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴_가격이_음수면_메뉴를_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Menu("양식 세트", -1, 양식))
                .withMessage(ErrorMessage.MENU_INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴 상품을 추가한다.")
    @Test
    void 메뉴_상품을_추가한다() {
        양식_세트.create(Arrays.asList(스테이크, 스파게티));

        assertThat(양식_세트.getMenuProducts()).hasSize(2);
    }

    @DisplayName("이미 등록된 메뉴는 추가되지 않는다.")
    @Test
    void 이미_등록된_메뉴는_추가되지_않는다() {
        양식_세트.create(Arrays.asList(스테이크, 스파게티));
        양식_세트.create(Arrays.asList(스파게티));

        assertThat(양식_세트.getMenuProducts()).hasSize(2);
    }

    @DisplayName("메뉴 상품 추가시 메뉴 상품의 총 금액의 합보다 메뉴의 가격이 클 수 없다.")
    @Test
    void 메뉴_상품_추가시_메뉴_상품의_총_금액의_합보다_메뉴의_가격이_클_수_없다() {
        Menu 양식_세트 = new Menu("양식 세트", 60000, 양식);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> 양식_세트.create(Arrays.asList(스테이크, 스파게티)))
                .withMessage(ErrorMessage.MENU_INVALID_PRICE.getMessage());
    }
}
