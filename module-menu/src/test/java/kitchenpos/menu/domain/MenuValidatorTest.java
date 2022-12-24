package kitchenpos.menu.domain;//package kitchenpos.menu.domain;
//
//import kitchenpos.product.repository.ProductRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static kitchenpos.menu.domain.fixture.MenuFixture.menuA;
//
//@SpringBootTest
//@Transactional
//class MenuValidatorTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    private MenuValidator menuValidator;
//
//    @BeforeEach
//    void setUp() {
//        menuValidator = new MenuValidator(productRepository);
//    }
//
//    @DisplayName("메뉴 상품 합 검증 성공")
//    @Test
//    void validate_success() {
//        Assertions.assertThatNoException().isThrownBy(() -> menuA(1L).validate(menuValidator));
//    }
//
//    @DisplayName("메뉴 상품 합 검증 실패")
//    @Test
//    void validate_fail() {
//        Assertions.assertThatThrownBy(() -> menuA(1L).validate(menuValidator))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}
