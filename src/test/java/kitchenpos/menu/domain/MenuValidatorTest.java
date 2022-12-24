package kitchenpos.menu.domain;

import kitchenpos.menu.domain.fixture.MenuFixture;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MenuValidatorTest {

    @Autowired
    private ProductRepository productRepository;

    private MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(productRepository);
    }

    @DisplayName("메뉴 상품 합 검증 성공")
    @Test
    void validate_success() {
        assertThatNoException().isThrownBy(() -> MenuFixture.menuA(1L).validate(menuValidator));
    }

    @DisplayName("메뉴 상품 합 검증 실패")
    @Test
    void validate_fail() {
        assertThatThrownBy(() -> MenuFixture.menuA(1L).validate(menuValidator))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
