//package kitchenpos.application;
//
//import kitchenpos.domain.MenuGroupTest;
//import kitchenpos.product.domain.ProductTest;
//import kitchenpos.menu.domain.Menu;
//import kitchenpos.menu.domain.repository.MenuRepository;
//import kitchenpos.menu.dto.CreateMenuRequest;
//import kitchenpos.menu.dto.MenuProductRequest;
//import kitchenpos.menu.dto.MenuResponse;
//import kitchenpos.menu.validator.MenuValidator;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//
//@ExtendWith(MockitoExtension.class)
//class MenuServiceTest {
//    public static MenuProductRequest 불고기버거상품 = new MenuProductRequest(ProductTest.불고기버거.getId(), 5);
//    public static MenuProductRequest 새우버거상품 = new MenuProductRequest(ProductTest.새우버거.getId(), 1);
//    public static Menu 불고기_새우버거_메뉴 = Menu.of("불고기버거 + 새우버거", BigDecimal.valueOf(2000.0), MenuGroupTest.햄버거_메뉴.getId());
//    public static CreateMenuRequest 메뉴_생성_요청 = new CreateMenuRequest("불고기버거 + 새우버거", BigDecimal.valueOf(5_000), MenuGroupTest.햄버거_메뉴.getId(),
//            Arrays.asList(불고기버거상품, 새우버거상품));
//
//    @Mock
//    private MenuRepository menuRepository;
//    @Mock
//    private MenuValidator menuValidator;
//    @InjectMocks
//    private MenuService menuService;
//
//    @Test
//    @DisplayName("메뉴 추가")
//    void create() {
//        // given
//        doNothing().when(menuValidator).validate(any());
//        given(menuRepository.save(any()))
//                .willReturn(불고기_새우버거_메뉴);
//        // when
//        final MenuResponse 메뉴_생성 = menuService.create(메뉴_생성_요청);
//        // then
//        assertThat(메뉴_생성).isInstanceOf(MenuResponse.class);
//    }
//
//    @Test
//    @DisplayName("메뉴 조회")
//    void list() {
//        // given
//        given(menuRepository.findAll())
//                .willReturn(Arrays.asList(불고기_새우버거_메뉴));
//        // when
//        final List<MenuResponse> menus = menuService.list();
//        // then
//        assertThat(menus).hasSize(1);
//    }
//}
