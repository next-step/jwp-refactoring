package kitchenpos.menu.validator;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static kitchenpos.common.Messages.*;
import static kitchenpos.menugroup.fixture.MenuGroupFixture.추천_메뉴;
import static kitchenpos.product.fixture.ProductFixture.치킨;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @InjectMocks
    private MenuValidator menuValidator;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    private MenuProductRequest 양념_치킨_요청값;

    @BeforeEach
    void setUp() {
        양념_치킨_요청값 = MenuProductRequest.of(치킨.getId(), 1);
    }

    @Test
    @DisplayName("메뉴를 생성할때 정상적으로 유효성 검사가 성공 된다")
    void validateCreateMenu() {
        MenuRequest 정상_메뉴 = MenuRequest.of(
                "추천 기본 메뉴",
                BigDecimal.valueOf(17_000),
                1L,
                Arrays.asList(양념_치킨_요청값)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(productRepository.findByIdIn(Arrays.asList(양념_치킨_요청값.getProductId()))).thenReturn(Arrays.asList(치킨));

        assertDoesNotThrow(() -> menuValidator.validateCreateMenu(정상_메뉴));
    }

    @Test
    @DisplayName("메뉴의 메뉴그룹 ID가 잘못된 경우 에러가 발생 된다.")
    void createMenuGroupNotExists() {
        MenuRequest 잘못된_그룹_메뉴_ID_요청값 = MenuRequest.of(
                "추천 기본 메뉴",
                BigDecimal.valueOf(17_000),
                9999L,
                Arrays.asList(양념_치킨_요청값)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(Boolean.FALSE);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> menuValidator.validateCreateMenu(잘못된_그룹_메뉴_ID_요청값))
                .withMessage(MENU_GROUP_NOT_EXISTS)
        ;
    }

    @Test
    @DisplayName("메뉴 등록시 상품정보가 조회가 되지 않은 경우 에러가 발생된다")
    void createProductFindInNoSuch() {
        MenuRequest 잘못된_상품_정보_요청값 = MenuRequest.of(
                "추천 기본 메뉴",
                BigDecimal.valueOf(17_000),
                추천_메뉴.getId(),
                Arrays.asList(양념_치킨_요청값)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(Boolean.TRUE);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> menuValidator.validateCreateMenu(잘못된_상품_정보_요청값))
                .withMessage(PRODUCT_FIND_IN_NO_SUCH);
    }

    @Test
    @DisplayName("메뉴 등록시 상품 가격의 합보다 메뉴가격이 큰 경우 실패 테스트")
    void create5() {
        MenuRequest 잘못된_상품_정보_요청값 = MenuRequest.of(
                "추천 기본 메뉴",
                BigDecimal.valueOf(100_000),
                추천_메뉴.getId(),
                Arrays.asList(양념_치킨_요청값)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(productRepository.findByIdIn(Arrays.asList(양념_치킨_요청값.getProductId()))).thenReturn(Arrays.asList(치킨));

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuValidator.validateCreateMenu(잘못된_상품_정보_요청값))
                .withMessage(MENU_PRICE_EXPENSIVE);
    }
}
