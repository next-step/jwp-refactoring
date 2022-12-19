package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.domain.MenuGroupTestFixture.메뉴_그룹_추천_메뉴;
import static kitchenpos.menu.domain.MenuProductTestFixture.메뉴_상품;
import static kitchenpos.menu.domain.MenuTest.메뉴;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;
import static kitchenpos.product.domain.ProductTestFixture.상품_콜라;
import static kitchenpos.product.domain.ProductTestFixture.상품_통다리;

@DisplayName("메뉴 생성 유효성 검증 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuCreateValidatorTest {

    private static final Product 통다리 = 상품_통다리(1L);
    private static final Product 콜라 = 상품_콜라(2L);

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuCreateValidator menuCreateValidator;

    @DisplayName("생성 예외 - 메뉴 그룹이 존재하지 않는 경우")
    @Test
    void 생성_예외_메뉴_그룹이_존재하지_않는_경우() {
        //given:
        final Menu 메뉴 = 메뉴(MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                null,
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(통다리.getId(), 5),
                        메뉴_상품(콜라.getId(), 1))));
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> menuCreateValidator.validate(메뉴));
    }

    @DisplayName("생성 예외 - 상품이 존재하지 않는 경우")
    @Test
    void 생성_예외_상품이_존재하지_않는_경우() {
        when(productRepository.findAllById(Arrays.asList(통다리.getId(), 콜라.getId()))).thenReturn(Collections.emptyList());
        final Menu 메뉴 = 메뉴(MenuName.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                메뉴_그룹_추천_메뉴(1L).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(통다리.getId(), 5),
                        메뉴_상품(콜라.getId(), 1))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> menuCreateValidator.validate(메뉴));
    }

    @DisplayName("생성 예외 - 메뉴의 가격이 상품 목록 가격의 합보다 큰 경우")
    @Test
    void 생성_예외_메뉴의_가격이_상품_목록의_가격_합보다_큰_경우() {
        //given:
        List<Product> productList = Arrays.asList(통다리, 콜라);
        when(productRepository.findAllById(Arrays.asList(통다리.getId(), 콜라.getId()))).thenReturn(productList);
        final int expensivePrice = 99999;
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> menuCreateValidator.validate(
                메뉴(MenuName.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.valueOf(expensivePrice)),
                        메뉴_그룹_추천_메뉴(1L).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(통다리.getId(), 5),
                                메뉴_상품(콜라.getId(), 1))))));
    }

}
