package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.domain.MenuFixture.메뉴;
import static kitchenpos.domain.MenuGroupFixture.메뉴그룹;
import static kitchenpos.domain.MenuProductFixture.메뉴상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴상품 테스트")
public class MenuProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @InjectMocks
    private MenuProductService menuProductService;

    private Menu 오일2인세트;
    private Product 알리오올리오;
    private Product 봉골레오일;
    private MenuProduct 오일2인세트_알리오올리오;
    private MenuProduct 오일2인세트_봉골레오일;

    @BeforeEach
    void setup() {
        MenuGroup 세트 = 메뉴그룹(1L, "세트");
        오일2인세트 = 메뉴(1L, "오일2인세트", 34000, 세트);

        알리오올리오 = ProductFixture.상품(1L, "알리오올리오", 17000);
        봉골레오일 = ProductFixture.상품(2L, "봉골레오일", 19000);

        오일2인세트_알리오올리오 = 메뉴상품(1L, 오일2인세트.getId(), 알리오올리오, 1);
        오일2인세트_봉골레오일 = 메뉴상품(2L, 오일2인세트.getId(), 봉골레오일, 1);

    }

    @DisplayName("메뉴 상품을 저장한다")
    @Test
    void 메뉴상품_저장_테스트() {
        // given
        given(productRepository.findById(알리오올리오.getId())).willReturn(Optional.ofNullable(알리오올리오));
        given(productRepository.findById(봉골레오일.getId())).willReturn(Optional.ofNullable(봉골레오일));
        given(menuProductRepository.saveAll(any())).willReturn(Arrays.asList(오일2인세트_알리오올리오, 오일2인세트_봉골레오일));

        // when
        List<MenuProductRequest> menuProductRequests = convertRequest(Arrays.asList(오일2인세트_알리오올리오, 오일2인세트_봉골레오일));
        List<MenuProductResponse> menuProductResponses = menuProductService.createAll(menuProductRequests, 오일2인세트.getId(), 오일2인세트.getPrice().value().intValue());

        // then
        assertAll(
                () -> assertThat(menuProductResponses).hasSize(2),
                () -> assertThat(menuProductResponses.stream()
                        .map(MenuProductResponse::getMenuId)
                        .collect(Collectors.toList())).containsOnly(오일2인세트.getId())
        );
    }

    private List<MenuProductRequest> convertRequest(List<MenuProduct> menuProducts) {
        return menuProducts.stream().map(MenuProductRequest::of).collect(Collectors.toList());
    }
}
