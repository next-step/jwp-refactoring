package kitchenpos.menu.service;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // given
        MenuGroup menuGroup = MenuGroup.of("튀김류");
        Product product = Product.of("양념치킨", Price.of(BigDecimal.valueOf(5000)));

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(5000), menuGroup.getId(), Collections.singletonList(menuProductRequest));

        given(menuGroupRepository.findById(menuGroup.getId())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(menuRepository.save(any())).willReturn(menuRequest.toMenu());

        // when
        MenuResponse response = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(response.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu savedMenu = Menu.of("치킨", Price.of(BigDecimal.valueOf(5000)), 1L, Collections.singletonList(MenuProduct.of(1L, Quantity.of(1L))));

        given(menuRepository.findAll()).willReturn(Collections.singletonList(savedMenu));

        // when
        List<MenuResponse> response = menuService.list();

        // then
        assertThat(response.size()).isEqualTo(1);
    }

    @DisplayName("메뉴 상품은 필수 입력 항목 저장소에 없음 예외")
    @Test
    void 메뉴_상품_존재_검증() {

        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(5000), 1L, Collections.singletonList(menuProductRequest));

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(MenuGroup.of("튀김류")));
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> menuService.create(menuRequest));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
    }

    @DisplayName("메뉴의 가격이 상품 가격의 합계보다 클 수 없음 예외")
    @Test
    void 메뉴_가격_검증() {
        Product product = Product.of("양념치킨", Price.of(BigDecimal.valueOf(50)));

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(5000), 1L, Collections.singletonList(menuProductRequest));

        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(MenuGroup.of("mock")));
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        Throwable thrown = catchThrowable(() -> menuService.create(menuRequest));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품 가격의 합계보다 클 수 없습니다.");
    }

    @DisplayName("메뉴 그룹은 필수 입력 항목 저장소에 없음 예외")
    @Test
    void 메뉴_그룹_존재_검증() {
        Product product = Product.of("양념치킨", Price.of(BigDecimal.valueOf(5000)));

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(5000), 1L, Collections.singletonList(menuProductRequest));

        Throwable thrown = catchThrowable(() -> menuService.create(menuRequest));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("해당 메뉴 그룹을 찾을 수 없습니다.");
    }
}
