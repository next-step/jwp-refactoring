package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.MenuProductTest.메뉴_상품_생성;
import static kitchenpos.domain.MenuTest.메뉴_생성;
import static kitchenpos.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성 시 0원 미만인 경우 예외가 발생해야 한다")
    @Test
    void createMenuByZeroPriceMenuTest() {
        // given
        Menu 메뉴 = 메뉴_생성(
                "0원 메뉴",
                0,
                0L,
                Collections.singletonList(메뉴_상품_생성(0L, 0L, 1))
        );

        // then
        메뉴_생성_실패됨(() -> menuService.create(메뉴));
    }

    @DisplayName("메뉴 생성 시 메뉴 그룹이 없거나 없는 메뉴 그룹으로 메뉴를 생성하면 예외가 발생해야 한다")
    @Test
    void createMenuByIllegalMenuGroupTest() {
        // given
        long noneExistMenuGroupId = 0L;
        Menu 없는_메뉴_그룹의_메뉴 = 메뉴_생성(
                "없는 메뉴 그룹의 메뉴",
                1_000,
                noneExistMenuGroupId,
                Collections.singletonList(메뉴_상품_생성(0L, 0L, 1))
        );
        Menu 메뉴_그룹_정보가_없는_메뉴 = 메뉴_생성(
                "메뉴 그룹 정보가 없는 메뉴",
                1_000,
                null,
                Collections.singletonList(메뉴_상품_생성(0L, 0L, 1))
        );
        when(menuGroupDao.existsById(noneExistMenuGroupId)).thenReturn(false);

        // then
        메뉴_생성_실패됨(() -> menuService.create(없는_메뉴_그룹의_메뉴));
        메뉴_생성_실패됨(() -> menuService.create(메뉴_그룹_정보가_없는_메뉴));
    }

    @DisplayName("메뉴의 가격과 메뉴에 포함된 상품의 전체 가격의 합이 일치하지 않는 경우 예외가 발생해야 한다")
    @Test
    void createMenuByNotIncludedProductsTest() {
        // given
        long existMenuGroupId = 1L;
        Product 상품 = 상품_생성("상품", 999);
        Menu 메뉴_가격과_포함된_상품의_가격이_맞지_않는_메뉴 = 메뉴_생성(
                "0원 메뉴",
                1_000,
                existMenuGroupId,
                Collections.singletonList(메뉴_상품_생성(0L, 0L, 1))
        );
        when(menuGroupDao.existsById(existMenuGroupId)).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(상품));

        // then
        메뉴_생성_실패됨(() -> menuService.create(메뉴_가격과_포함된_상품의_가격이_맞지_않는_메뉴));
    }

    @DisplayName("정상 상태의 메뉴 생성 시 정상 생성되어야 한다")
    @Test
    void createMenuTest() {
        // given
        long existMenuGroupId = 1L;
        Product 상품 = 상품_생성("상품", 1_000);
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(0L, 0L, 2);
        Menu 메뉴 = 메뉴_생성(
                "0원 메뉴",
                1_000,
                existMenuGroupId,
                Collections.singletonList(메뉴_상품)
        );
        when(menuGroupDao.existsById(existMenuGroupId)).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(상품));
        when(menuDao.save(메뉴)).thenReturn(메뉴);
        when(menuProductDao.save(메뉴_상품)).thenReturn(메뉴_상품);

        // when
        Menu 생성_메뉴 = menuService.create(메뉴);

        // then
        메뉴_생성_성공됨(생성_메뉴, 메뉴);
    }

    @DisplayName("메뉴 전체 조회는 정상 동작해야 한다")
    @Test
    void findAllMenuTest() {
        // given
        List<Menu> 메뉴_리스트 = Arrays.asList(
                메뉴_생성("메뉴 1", 1_000, null, null),
                메뉴_생성("메뉴 2", 1_000, null, null),
                메뉴_생성("메뉴 3", 1_000, null, null),
                메뉴_생성("메뉴 4", 1_000, null, null),
                메뉴_생성("메뉴 5", 1_000, null, null)
        );
        when(menuDao.findAll()).thenReturn(메뉴_리스트);

        // when
        List<Menu> 메뉴_조회_결과 = menuService.list();

        // then
        assertThat(메뉴_조회_결과.size()).isGreaterThanOrEqualTo(메뉴_조회_결과.size());
        assertThat(메뉴_조회_결과).containsAll(메뉴_조회_결과);
    }

    void 메뉴_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 메뉴_생성_성공됨(Menu source, Menu target) {
        assertThat(source.getName()).isEqualTo(target.getName());
        assertThat(source.getPrice()).isEqualTo(target.getPrice());
        assertThat(source.getMenuGroupId()).isEqualTo(target.getMenuGroupId());
        assertThat(source.getMenuProducts()).isEqualTo(target.getMenuProducts());
    }
}
