package kitchenpos.service;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductResponse;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("메뉴 관련 테스트")
@Transactional
@SpringBootTest
public class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @DisplayName("메뉴 그룹 저장 테스트")
    @Test
    void saveMenuGroup() {
        MenuGroupResponse menuGroupResponse = menuGroupService.save(new MenuGroupRequest("인기 메뉴"));
        assertThat(menuGroupResponse.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 조회 테스트")
    @Test
    void findMenuGroup() {
        menuGroupService.save(new MenuGroupRequest("인기 메뉴"));

        List<MenuGroupResponse> menuGroups = menuGroupService.findAll();

        assertThat(menuGroups).hasSize(1);
        assertThat(menuGroups).extracting("name")
                .containsExactly("인기 메뉴");
    }

    @DisplayName("메뉴 저장 테스트")
    @Test
    void saveMenu() {
        MenuResponse menuResponse = createMenu();

        assertThat(menuResponse.getId()).isNotNull();
    }

    @DisplayName("메뉴 조회 테스트")
    @Test
    void findMenu() {
        MenuResponse menuResponse = createMenu();

        List<MenuResponse> menus = menuService.findAll();

        assertThat(menus).hasSize(1);
        assertThat(menus).extracting(MenuResponse::getMenuGroupResponse)
                .extracting("name")
                .containsExactly("인기 메뉴");
        assertThat(menus).extracting("name")
                .containsExactly("두마리 치킨");
        assertThat(menus).extracting(menu -> menu.getProductResponses().stream().map(ProductResponse::getName).collect(toList()))
                .containsExactly(asList("후라이드 치킨", "양념 치킨"));
    }

    private MenuResponse createMenu() {
        MenuGroupResponse menuGroupResponse = menuGroupService.save(new MenuGroupRequest("인기 메뉴"));
        ProductResponse 후라이드_치킨 = productService.save(new Product("후라이드 치킨", new Price(18000)));
        ProductResponse 양념_치킨 = productService.save(new Product("양념 치킨", new Price(20000)));
        MenuRequest menuRequest = new MenuRequest("두마리 치킨", 20000, menuGroupResponse.getId(),
                asList(new MenuProductRequest(후라이드_치킨.getId(), 2), new MenuProductRequest(양념_치킨.getId(), 3)));
        return menuService.save(menuRequest);
    }
}
