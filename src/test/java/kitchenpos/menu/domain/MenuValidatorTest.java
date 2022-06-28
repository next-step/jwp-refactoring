package kitchenpos.menu.domain;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.menu.domain.repository.MenuProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("메뉴관련 Validator 기능 테스트")
@DataJpaTest
@Import(MenuValidator.class)
class MenuValidatorTest {

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuValidator menuValidator;

    @DisplayName("메뉴 가격은 총 금액을 넘을 수 없다.")
    @Test
    void validate_over_amount() {
        //given
        MenuProduct 간장치킨 = menuProductRepository.findById(5L).orElseThrow(IllegalArgumentException::new);
        MenuProduct 순살치킨 = menuProductRepository.findById(6L).orElseThrow(IllegalArgumentException::new);
        MenuProducts menuProducts = new MenuProducts();
        menuProducts.add(간장치킨);
        menuProducts.add(순살치킨);

        Menu menu = 메뉴_만들기("세트", 90_000, null, menuProducts);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validate(menu));
    }


}
