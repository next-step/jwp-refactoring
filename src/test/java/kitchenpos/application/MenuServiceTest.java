package kitchenpos.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.menu.exception.NoMenuGroupException;
import kitchenpos.menuproduct.domain.MenuProductRepository;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.menuproduct.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    MenuGroupRepository menuGroupRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    MenuProductRepository menuProductRepository;
    @Mock
    ApplicationEventPublisher publisher;

    MenuService menuService;

    MenuGroup 한마리메뉴;
    Product 후라이드치킨;
    Product 양념치킨;
    MenuProduct 반반세트후라이드;
    MenuProduct 반반세트양념;
    Menu 반반세트;

    MenuRequest 반반세트요청;
    MenuProductRequest 반반세트후라이드요청;
    MenuProductRequest 반반세트양념요청;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(18000L));
        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(19000L));
        반반세트후라이드 = new MenuProduct(후라이드치킨, 1L);
        반반세트양념 = new MenuProduct(양념치킨, 1L);


        반반세트후라이드요청 = new MenuProductRequest(후라이드치킨.getId(), 1L);
        반반세트양념요청 = new MenuProductRequest(양념치킨.getId(), 1L);
        반반세트요청 = new MenuRequest("반반세트", BigDecimal.valueOf(35000L), 한마리메뉴.getId(),
                Arrays.asList(반반세트후라이드요청, 반반세트양념요청));

        반반세트 = new Menu(1L, 반반세트요청.getName(), 반반세트요청.getPrice(), 반반세트요청.getMenuGroupId());

        menuService = new MenuService(menuRepository, menuGroupRepository, publisher);
    }

    @Test
    @DisplayName("정상적으로 메뉴 등록")
    void 정상적으로_메뉴_등록() {
        //given
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(menuRepository.save(any())).willReturn(반반세트);

        //when
        MenuResponse savedMenuResponse = menuService.create(반반세트요청);

        //then
        assertThat(savedMenuResponse.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("등록시 메뉴의 금액 입력이 잘못된 경우")
    void 메뉴_금액_오류() {
        //given
        반반세트요청 = new MenuRequest("반반세트", BigDecimal.valueOf(-1000L), 한마리메뉴.getId(),
                Arrays.asList(반반세트후라이드요청, 반반세트양념요청));

        //when, then
        assertThatThrownBy(() -> menuService.create(반반세트요청)).isInstanceOf(IllegalMenuPriceException.class);
    }

    @Test
    @DisplayName("등록시 메뉴의 메뉴 그룹이 잘못 지정된 경우")
    void 메뉴_그룹_오류() {
        //given
        given(menuGroupRepository.existsById(anyLong())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> menuService.create(반반세트요청)).isInstanceOf(NoMenuGroupException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 불러옴")
    void 메뉴_목록을_불러옴() {
        MenuGroup 파격할인메뉴 = new MenuGroup(1L, "파격할인메뉴");
        //given
        given(menuRepository.findAll()).willReturn(Arrays.asList(
                new Menu(1L, "반반세트", BigDecimal.valueOf(40000L), 한마리메뉴.getId()),
                new Menu(2L, "반반세트", BigDecimal.valueOf(1000L), 파격할인메뉴.getId())
        ));

        //when, then
        assertThat(menuService.list()).hasSize(2);
    }
}
