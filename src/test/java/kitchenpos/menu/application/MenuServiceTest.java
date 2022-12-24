package kitchenpos.menu.application;

import kitchenpos.common.exception.NoSuchDataException;
import kitchenpos.menu.domain.*;
import kitchenpos.product.application.MenuProductService;
import kitchenpos.product.domain.MenuProductRepository;
import kitchenpos.product.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.menu.domain.MenuFixture.메뉴;
import static kitchenpos.menu.domain.MenuGroupFixture.메뉴그룹;
import static kitchenpos.product.domain.MenuProductFixture.메뉴상품;
import static kitchenpos.product.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 테스트")
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuProductRepository menuProductRepository;

    private MenuProductService menuProductService;
    private MenuService menuService;

    private Product 알리오올리오;
    private Product 봉골레오일;

    private MenuGroup 세트;
    private MenuGroup 코스;

    private Menu 오일2인세트;
    private Menu 풀코스;

    private MenuProduct 오일2인세트_알리오올리오;
    private MenuProduct 오일2인세트_봉골레오일;


    @BeforeEach
    void setup() {
        알리오올리오 = 상품(1L, "알리오올리오", 17000);
        봉골레오일 = 상품(2L, "봉골레오일", 19000);

        세트 = 메뉴그룹(1L, "세트");
        코스 = 메뉴그룹(2L, "코스");

        오일2인세트 = 메뉴(1L, "오일2인세트", 34000, 세트);
        풀코스 = 메뉴(2L, "풀코스", 62000, 코스);

        오일2인세트_알리오올리오 = 메뉴상품(1L, 오일2인세트.getId(), 알리오올리오, 1);
        오일2인세트_봉골레오일 = 메뉴상품(2L, 오일2인세트.getId(), 봉골레오일, 1);

        menuProductService = new MenuProductService(productRepository, menuProductRepository);
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductService);
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void 메뉴_생성() {
        // given
        List<MenuProduct> menuProduct = Arrays.asList(오일2인세트_알리오올리오, 오일2인세트_봉골레오일);

        given(menuGroupRepository.findById(세트.getId())).willReturn(Optional.ofNullable(세트));
        given(menuRepository.save(any())).willReturn(오일2인세트);
        given(productRepository.findById(알리오올리오.getId())).willReturn(Optional.ofNullable(알리오올리오));
        given(productRepository.findById(봉골레오일.getId())).willReturn(Optional.ofNullable(봉골레오일));
        given(menuProductRepository.saveAll(any())).willReturn(Arrays.asList(오일2인세트_알리오올리오, 오일2인세트_봉골레오일));

        // when
        MenuRequest menuRequest = new MenuRequest(오일2인세트.getName(), 오일2인세트.getPrice().value().intValue(), 세트.getId(), convertRequest(menuProduct));
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        verify(menuRepository).save(any());
        assertAll(
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(세트.getId()),
                () -> assertThat(menuResponse.getPrice().value()).isEqualTo(new BigDecimal(34000)),
                () -> assertThat(menuResponse.getMenuProducts()).hasSize(2)
        );
    }

    @DisplayName("전체 메뉴 목록을 조회한다")
    @Test
    void 전체_메뉴_목록_조회() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(오일2인세트, 풀코스));

        // when
        List<MenuResponse> menus = menuService.list();
        List<String> menuProductNames = menus.stream().map(MenuResponse::getName).collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(menus).hasSize(2),
                () -> assertThat(menuProductNames).containsExactly(오일2인세트.getName(), 풀코스.getName())
        );
    }

    @DisplayName("가격이 음수인 메뉴를 생성한다")
    @Test
    void 가격이_음수인_메뉴_생성() {
        // given
        given(menuGroupRepository.findById(세트.getId())).willReturn(Optional.ofNullable(세트));

        // when
        List<MenuProductRequest> menuProductRequests = convertRequest(Arrays.asList(오일2인세트_알리오올리오, 오일2인세트_봉골레오일));
        MenuRequest menuRequest = new MenuRequest(오일2인세트.getName(), -17000, 세트.getId(), menuProductRequests);

        // then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹 정보가 없는 메뉴를 생성한다")
    @Test
    void 메뉴_그룹_정보가_없는_메뉴_생성() {
        // given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(MenuProductRequest.of(오일2인세트_알리오올리오), MenuProductRequest.of(오일2인세트_봉골레오일));
        MenuRequest menuRequest = new MenuRequest(오일2인세트.getName(), 오일2인세트.getPrice().value().intValue(), 100L, menuProductRequests);
        given(menuGroupRepository.findById(menuRequest.getMenuGroupId())).willReturn(Optional.ofNullable(null));

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("상품 정보가 없는 메뉴를 생성한다")
    @Test
    void 상품_정보가_없는_메뉴_생성() {
        // given
        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(세트));
        given(menuRepository.save(any())).willReturn(오일2인세트);
        given(productRepository.findById(알리오올리오.getId())).willReturn(Optional.ofNullable(알리오올리오));
        given(productRepository.findById(봉골레오일.getId())).willReturn(Optional.ofNullable(null));

        // when
        List<MenuProduct> menuProducts = Arrays.asList(오일2인세트_알리오올리오, 오일2인세트_봉골레오일);
        MenuRequest menuRequest = new MenuRequest(오일2인세트.getName(), 오일2인세트.getPrice().value().intValue(), 세트.getId(), convertRequest(menuProducts));

        // then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("단일 상품 가격 합보다 메뉴 가격이 큰 메뉴를 생성한다")
    @Test
    void 단일_상품_가격_합보다_큰_메뉴_가격의_메뉴_생성() {
        // given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(MenuProductRequest.of(오일2인세트_알리오올리오), MenuProductRequest.of(오일2인세트_봉골레오일));
        MenuRequest menuRequest = new MenuRequest(오일2인세트.getName(), 100000, 세트.getId(), menuProductRequests);

        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(세트));
        given(menuRepository.save(any())).willReturn(new Menu(menuRequest.getName(), menuRequest.getPrice(), 세트));
        given(productRepository.findById(알리오올리오.getId())).willReturn(Optional.ofNullable(알리오올리오));
        given(productRepository.findById(봉골레오일.getId())).willReturn(Optional.ofNullable(봉골레오일));


        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private List<MenuProductRequest> convertRequest(List<MenuProduct> menuProducts) {
        return menuProducts.stream().map(MenuProductRequest::of).collect(Collectors.toList());
    }
}
