package kitchenpos.application;


import static kitchenpos.application.fixture.MenuFixture.요청_메뉴;
import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static kitchenpos.application.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.application.fixture.MenuProductFixture.요청_메뉴상품_치킨;
import static kitchenpos.application.fixture.ProductFixture.후리이드치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관리 기능")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("`메뉴`를 등록할 수 있다.")
    void create() {
        // given
        Product 치킨 = 후리이드치킨(1L);
        MenuProduct 메뉴_치킨 = 메뉴상품(치킨);
        MenuGroup 메뉴_그룹 = 메뉴그룹_치킨류();
        MenuRequest menuRequest = 요청_메뉴("메뉴이름", 14000, 1L, Collections.singletonList(요청_메뉴상품_치킨()));
        Menu 등록_메뉴 = menuRequest.toMenu(메뉴_그룹, Collections.singletonList(메뉴_치킨));

        given(menuGroupRepository.findById(any())).willReturn(Optional.of(메뉴_그룹));
        given(productRepository.findAllById(anyList())).willReturn(Collections.singletonList(치킨));
        given(menuRepository.save(any())).willReturn(등록_메뉴);

        // when
        MenuResponse 등록된_메뉴 = menuService.create(menuRequest);

        // then
        메뉴등록_됨(등록된_메뉴);
    }

    @Test
    @DisplayName("`메뉴`의 목록을 조회할 수 있다.")
    void 메뉴_목록_조회() {
        // given
        Product 치킨 = 후리이드치킨(1L);
        MenuProduct 메뉴_치킨 = 메뉴상품(치킨);
        MenuGroup 메뉴_그룹 = 메뉴그룹_치킨류();
        Menu 등록_메뉴 = Menu.of("메뉴", 14000, 메뉴_그룹, Collections.singletonList(메뉴_치킨));
        given(menuRepository.findAll()).willReturn(Collections.singletonList(등록_메뉴));

        // when
        List<MenuResponse> 메뉴목록 = menuService.list();

        // then
        메뉴목록_조회됨(메뉴목록);
    }

    @Test
    @DisplayName("`메뉴`가 속할 `메뉴그룹`이 필수로 있어야 한다.")
    void 메뉴는_메뉴그룹이_없으면_에러() {
        // given
        MenuRequest menuRequest = 요청_메뉴("메뉴이름", 14000, 1L, Collections.singletonList(요청_메뉴상품_치킨()));
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable actual = () -> menuService.create(menuRequest);

        // then
        메뉴생성_메뉴그룹_없음_실패(actual);
    }

    private void 메뉴생성_메뉴그룹_없음_실패(ThrowingCallable actual) {
        assertThatThrownBy(actual).isInstanceOf(NotFoundException.class);
    }

    private void 메뉴등록_됨(MenuResponse menuResponse) {
        assertThat(menuResponse).isNotNull();
    }

    private void 메뉴목록_조회됨(List<MenuResponse> 메뉴목록) {
        assertThat(메뉴목록).isNotEmpty();
    }
}
