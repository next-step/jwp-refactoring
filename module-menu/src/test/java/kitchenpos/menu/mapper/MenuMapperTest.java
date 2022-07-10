package kitchenpos.menu.mapper;

import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menuGroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static kitchenpos.generator.CommonGenerator.가격_생성;
import static kitchenpos.generator.MenuGenerator.메뉴_상품_생성_요청;
import static kitchenpos.generator.MenuGenerator.메뉴_생성_요청;
import static kitchenpos.generator.ProductGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MenuMapperTest {

    @MockBean
    private MenuGroupRepository menuGroupRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private MenuMapper menuMapper;

    private Product 상품 = 상품_생성("상품", 가격_생성(1_000));
    private MenuProductRequest 메뉴_상품_생성_요청 = 메뉴_상품_생성_요청(1, 2L);

    @DisplayName("메뉴 생성 요청시 없는 메뉴 그룹 정보로 메뉴를 생성하면 예외가 발생해야 한다")
    @Test
    void createMenuByNotAssociateMenuGroup() {
        // when
        MenuCreateRequest 메뉴_생성_요청 = 메뉴_생성_요청("메뉴 상품이 null 인 메뉴", 1_000, 0L, Collections.singletonList(메뉴_상품_생성_요청));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->  menuMapper.mapFrom(메뉴_생성_요청));
    }

    @DisplayName("메뉴 생성 요청시 메뉴 상품이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void createMenuByNotIncludeMenuProducts() {
        // given
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        MenuCreateRequest 메뉴_생성_요청 = 메뉴_생성_요청("메뉴 상품이 null 인 메뉴", 1_000, 0L, Collections.emptyList());

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->  menuMapper.mapFrom(메뉴_생성_요청));
    }

    @DisplayName("메뉴 유효성 검사시 메뉴 상품 목록의 전체 가격과 메뉴의 가격이 일치하지 않으면 예외가 발생해야 한다")
    @Test
    void createMenuByNotMatchedTotalMenuProductPriceAndMenuPrice() {
        // given
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(상품));
        MenuCreateRequest 메뉴_생성_요청 = 메뉴_생성_요청("메뉴 상품이 null 인 메뉴", 1_000, 0L, Collections.singletonList(메뉴_상품_생성_요청));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->  menuMapper.mapFrom(메뉴_생성_요청));
    }

    @DisplayName("정상 상태의 메뉴 생성 시 메뉴 객체가 생성되어야 한다")
    @Test
    void createMenuTest() {
        // given
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(상품));
        MenuCreateRequest 메뉴_생성_요청 = 메뉴_생성_요청("메뉴 상품이 null 인 메뉴", 2_000, 0L, Collections.singletonList(메뉴_상품_생성_요청));

        // then
        assertThatNoException().isThrownBy(() ->  menuMapper.mapFrom(메뉴_생성_요청));
    }
}