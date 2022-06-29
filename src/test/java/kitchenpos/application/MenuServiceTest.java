package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.fixture.UnitTestFixture;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductService productService;

    @InjectMocks
    private MenuService menuService;

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 메뉴를_등록할_수_있어야_한다() {
        // given
        final MenuRequest given = new MenuRequest(
                "돼지모듬",
                new Price(43000L),
                식당_포스.구이류.getId(),
                Arrays.asList(
                        new MenuProductRequest(식당_포스.삼겹살.getId(), 2L),
                        new MenuProductRequest(식당_포스.목살.getId(), 1L)));
        final Menu expected = new Menu(1L);
        when(menuGroupService.existsById(식당_포스.구이류.getId())).thenReturn(true);
        when(productService.getById(식당_포스.삼겹살.getId())).thenReturn(식당_포스.삼겹살);
        when(productService.getById(식당_포스.목살.getId())).thenReturn(식당_포스.목살);
        when(menuRepository.save(any())).thenReturn(expected);

        // when
        final MenuResponse actual = menuService.create(given);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void 메뉴_등록_시_메뉴_그룹이_존재하지_않으면_에러가_발생해야_한다() {
        // given
        final Long invalidMenuGroupId = -1L;
        final MenuRequest given = new MenuRequest(
                "돼지모듬",
                new Price(43000L),
                -1L,
                Arrays.asList(
                        new MenuProductRequest(식당_포스.삼겹살.getId(), 2L),
                        new MenuProductRequest(식당_포스.목살.getId(), 1L)));
        when(menuGroupService.existsById(invalidMenuGroupId)).thenReturn(false);

        // when and then
        assertThatThrownBy(() -> menuService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록_시_상품이_없으면_에러가_발생해야_한다() {
        // given
        final Long invalidProductId = -1L;
        final MenuRequest given = new MenuRequest(
                "돼지모듬",
                new Price(43000L),
                식당_포스.구이류.getId(),
                Arrays.asList(
                        new MenuProductRequest(식당_포스.삼겹살.getId(), 2L),
                        new MenuProductRequest(invalidProductId, 1L)));
        when(menuRepository.existsById(식당_포스.구이류.getId())).thenReturn(true);
        when(productService.getById(식당_포스.삼겹살.getId())).thenReturn(식당_포스.삼겹살);
        when(productService.getById(invalidProductId)).thenThrow(new IllegalArgumentException());

        // when and then
        assertThatThrownBy(() -> menuService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록_시_메뉴_가격이_상품들의_금액_총합보다_크면_에러가_발생해야_한다() {
        // given
        final Menu given = new Menu(
                1L,
                "돼지모듬",
                BigDecimal.valueOf(1000000L),
                식당_포스.구이류.getId(),
                Arrays.asList(식당_포스.돼지모듬_삼겹살, 식당_포스.돼지모듬_목살));
        when(menuGroupDao.existsById(식당_포스.구이류.getId())).thenReturn(true);
        when(productDao.findById(식당_포스.삼겹살.getId())).thenReturn(Optional.of(식당_포스.삼겹살));
        when(productDao.findById(식당_포스.목살.getId())).thenReturn(Optional.of(식당_포스.목살));

        // when and then
        assertThatThrownBy(() -> menuService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회할_수_있어야_한다() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(식당_포스.돼지모듬, 식당_포스.김치찌개정식));

        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual).containsExactly(식당_포스.돼지모듬, 식당_포스.김치찌개정식);
    }
}
