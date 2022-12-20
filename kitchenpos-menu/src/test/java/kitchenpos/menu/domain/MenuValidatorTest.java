package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuGroupTestFixture.면류;
import static kitchenpos.menu.domain.MenuProductTestFixture.짜장면_1그릇_요청;
import static kitchenpos.menu.domain.MenuTestFixture.menuRequest;
import static kitchenpos.product.domain.ProductTestFixture.짜장면;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴 등록시 메뉴 그룹은 필수이다.")
    void createMenuByMenuGroupNotExist() {
        // given
        MenuRequest menuRequest = menuRequest("짜장1_요청", BigDecimal.valueOf(9_000L),
                MenuGroupTestFixture.면류.id(), Collections.singletonList(MenuProductTestFixture.짜장면_1그릇_요청));
        Menu actual = menuRequest.toEntity();
        given(menuGroupRepository.existsById(면류.id())).willReturn(false);


        // when & then
        assertThatThrownBy(() -> menuValidator.validate(actual))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("메뉴 그룹을 찾을 수 없습니다. ID : 1");
    }

    @Test
    @DisplayName("상품으로 등록되어 있지 않은 메뉴를 등록한다.")
    void createMenuByNotCreateProduct() {
        // given
        MenuRequest menuRequest = menuRequest("짜장1_요청", BigDecimal.valueOf(9_000L),
                면류.id(), Collections.singletonList(MenuProductTestFixture.짜장면_1그릇_요청));
        Menu actual = menuRequest.toEntity();
        given(menuGroupRepository.existsById(면류.id())).willReturn(true);
        given(productRepository.findById(짜장면.id())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(actual))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("상품을 찾을 수 없습니다. ID : 1");
    }

    @Test
    @DisplayName("메뉴 가격은 메뉴 상품가격의 합계보다 클 수 없다.")
    void createMenuByNotMoreThanProductsSum() {
        // given
        MenuRequest menuRequest = menuRequest("짜장1_요청", BigDecimal.valueOf(9_000L),
                면류.id(), Collections.singletonList(짜장면_1그릇_요청));
        Menu actual = menuRequest.toEntity();
        given(menuGroupRepository.existsById(면류.id())).willReturn(true);
        given(productRepository.findById(짜장면.id())).willReturn(Optional.of(
                짜장면));


        // when & then
        assertThatThrownBy(() -> menuValidator.validate(actual))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("상품 총 금액이 메뉴의 가격 보다 클 수 없습니다.");
    }
}
