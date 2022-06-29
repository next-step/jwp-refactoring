package kitchenpos.menu.domain;

import kitchenpos.DomainServiceTest;
import kitchenpos.core.domain.Price;
import kitchenpos.core.exception.InvalidPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.NotFoundMenuGroupException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.NotFoundProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuCreatingValidator 클래스 테스트")
class MenuCreatingValidatorTest extends DomainServiceTest {

    @Autowired
    private MenuCreatingValidator menuCreatingValidator;

    private MenuGroup 신메뉴;
    private Product 강정치킨;

    @BeforeEach
    void setUp(@Autowired MenuGroupRepository menuGroupRepository,
               @Autowired ProductRepository productRepository) {
        신메뉴 = menuGroupRepository.save(new MenuGroup("신메뉴"));
        강정치킨 = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(15_000L)));
    }

    @DisplayName("메뉴 생성에 대한 정책을 검증한다.")
    @Test
    void validate() {
        assertThatNoException().isThrownBy(() -> {
            menuCreatingValidator.validate(신메뉴.getId(), new Price(BigDecimal.valueOf(15_000L)),
                                           Arrays.asList(new MenuProduct(강정치킨.getId(), 1L)));
        });
    }

    @DisplayName("존재하지 않는 메뉴그룹으로 메뉴 생성에 대한 정책을 검증한다.")
    @Test
    void validateWithNotFoundMenuGroup() {
        Long 존재하지_않는_메뉴그룹_id = Long.MAX_VALUE;
        assertThatThrownBy(() -> {
            menuCreatingValidator.validate(존재하지_않는_메뉴그룹_id, new Price(BigDecimal.valueOf(16_000L)),
                                           Arrays.asList(new MenuProduct(강정치킨.getId(), 1L)));
        }).isInstanceOf(NotFoundMenuGroupException.class)
        .hasMessageContaining("메뉴 그룹을 찾을 수 없습니다.");
    }

    @DisplayName("존재하지 않는 상품으로 메뉴 생성에 대한 정책을 검증한다.")
    @Test
    void validateWithNotFoundProduct() {
        Long 존재하지_않는_상품_id = Long.MAX_VALUE;
        assertThatThrownBy(() -> {
            menuCreatingValidator.validate(신메뉴.getId(), new Price(BigDecimal.valueOf(16_000L)),
                                           Arrays.asList(new MenuProduct(존재하지_않는_상품_id, 1L)));
        }).isInstanceOf(NotFoundProductException.class)
        .hasMessageContaining("상품을 찾을 수 없습니다.");
    }

    @DisplayName("상품 가격보다 높은 메뉴 가격으로 메뉴 생성에 대한 정책을 검증한다.")
    @Test
    void validateWithOverPrice() {
        assertThatThrownBy(() -> {
            menuCreatingValidator.validate(신메뉴.getId(), new Price(BigDecimal.valueOf(16_000L)),
                                           Arrays.asList(new MenuProduct(강정치킨.getId(), 1L)));
        }).isInstanceOf(InvalidPriceException.class)
        .hasMessageContaining("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다.");
    }
}
