package kitchenpos.menu.domain;

import kitchenpos.common.vo.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.nameMenuA;
import static kitchenpos.common.fixture.NameFixture.nameProductA;
import static kitchenpos.common.fixture.PriceFixture.priceProductA;
import static kitchenpos.menu.domain.fixture.MenuFixture.menuA;
import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static kitchenpos.menu.domain.fixture.MenuProductFixture.menuProductA;
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
        Product product = productRepository.save(new Product(1L, nameProductA(), priceProductA()));
        Menu menu = menuA(product);
        assertThatNoException().isThrownBy(() -> menu.validate(menuValidator));
    }

    @DisplayName("메뉴 상품 합 검증 실패")
    @Test
    void validate_fail() {
        Product product = productRepository.save(new Product(1L, nameProductA(), priceProductA()));
        Menu menu = new Menu(nameMenuA(), new Price(BigDecimal.TEN), menuGroupA(), new MenuProducts(singletonList(menuProductA(product))));
        assertThatThrownBy(() -> menu.validate(menuValidator))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
