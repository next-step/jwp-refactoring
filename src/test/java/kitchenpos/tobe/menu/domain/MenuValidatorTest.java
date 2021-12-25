package kitchenpos.tobe.menu.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.tobe.fixture.MenuFixture;
import kitchenpos.tobe.fixture.MenuGroupFixture;
import kitchenpos.tobe.fixture.MenuProductFixture;
import kitchenpos.tobe.fixture.MenuProductsFixture;
import kitchenpos.tobe.fixture.ProductFixture;
import kitchenpos.tobe.product.domain.Product;
import kitchenpos.tobe.product.domain.ProductRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator validator;

    private List<Product> products;
    private MenuProducts menuProducts;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixture.of(1L, "아이폰");

        final Product iPhone13 = ProductFixture.of(1L, "아이폰13", BigDecimal.valueOf(1_000L));
        final Product appleCare = ProductFixture.of(2L, "애플케어", BigDecimal.valueOf(500L));
        products = Arrays.asList(iPhone13, appleCare);

        menuProducts = MenuProductsFixture.of(
            MenuProductFixture.of(iPhone13.getId(), 1_000L, 1L),
            MenuProductFixture.of(appleCare.getId(), 500L, 1L)
        );
    }

    @DisplayName("메뉴 등록을 검증할 수 있다.")
    @Test
    void validate() {
        // given
        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
        given(productRepository.findAllByIdIn(anyList())).willReturn(products);

        // when
        final ThrowableAssert.ThrowingCallable request = () -> MenuFixture.of(
            1L,
            "아이폰13",
            1_500L,
            menuProducts,
            menuGroup.getId(),
            validator
        );

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void validateFailIsOverpriced() {
        // given
        final long price = Long.MAX_VALUE;

        // when
        final ThrowableAssert.ThrowingCallable request = () -> MenuFixture.of(
            1L,
            "아이폰13",
            price,
            menuProducts,
            menuGroup.getId(),
            validator
        );

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 특정 메뉴 그룹에 속하는지 검증할 수 있다.")
    @Test
    void validateFailNoSuchMenuGroup() {
        // given
        final Long noMenuGroupId = 0L;

        // when
        final ThrowableAssert.ThrowingCallable request = () -> MenuFixture.of(
            1L,
            "아이폰13",
            1_500L,
            menuProducts,
            noMenuGroupId,
            validator
        );

        // then
        assertThatThrownBy(request).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("메뉴에 등록 요청된 상품이 존재하는지 검증할 수 있다.")
    @Test
    void validateFailNoSuchProducts() {
        // given
        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));

        // when
        final ThrowableAssert.ThrowingCallable request = () -> MenuFixture.of(
            1L,
            "아이폰13",
            1_500L,
            menuProducts,
            menuGroup.getId(),
            validator
        );

        // then
        assertThatThrownBy(request).isInstanceOf(NoSuchElementException.class);
    }
}
