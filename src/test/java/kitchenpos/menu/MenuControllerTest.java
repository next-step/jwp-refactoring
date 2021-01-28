package kitchenpos.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.common.BaseContollerTest;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.ProductTestSupport;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends BaseContollerTest {

    private Menu menu;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    public void setup() {
        super.setup();
        this.menu = MenuTestSupport.createMenu("더 맛있는 후라이드 치킨", 20000, new MenuGroup());
        this.addMenuProduct(this.menu);
    }

    @Test
    @DisplayName("새로운 메뉴를 등록합니다.")
    void createMenu() throws Exception {
        MenuRequest menuRequest = new MenuRequest(menu.getName(), menu.getPrice()
                , menu.getMenuGroup().getId()
                , menu.getMenuProducts().stream().map(MenuProduct::getSeq).collect(Collectors.toList()));
        메뉴_생성_요청(menuRequest, status().isCreated());
    }

    @Test
    @DisplayName("메뉴의 가격이 없는 경우 등록 시 오류가 발생합니다.")
    void createMenuNoPriceOccurredError() {
        MenuRequest menuRequest = new MenuRequest(menu.getName(), null
                , menu.getMenuGroup().getId()
                , menu.getMenuProducts().stream().map(MenuProduct::getSeq).collect(Collectors.toList()));

        assertThatThrownBy(() -> {
            메뉴_생성_요청(menuRequest, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    @DisplayName("메뉴에 등록되지 않은 상품이 있는 경우 등록 시 오류가 발생합니다.")
    void createMenuNoRegistProductOccurredError() {
        MenuRequest menuRequest = new MenuRequest(menu.getName(), menu.getPrice()
                , menu.getMenuGroup().getId()
                , Arrays.asList(new Long[]{100L}));

        assertThatThrownBy(() -> {
            메뉴_생성_요청(menuRequest, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    @DisplayName("모든 메뉴 목록을 조회합니다.")
    void getProducts() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(".menuProducts").exists())
                .andReturn();
    }

    private ResultActions 메뉴_생성_요청(MenuRequest menuRequest, ResultMatcher created) throws Exception {
        return this.mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(menuRequest)
                ))
                .andDo(print())
                .andExpect(created);
    }

    /**
     * 메뉴에 메뉴상품을 추가합니다.
     * @param menu
     */
    private void addMenuProduct(Menu menu) {
        menu.setMenuGroup(this.menuGroupRepository.save(new MenuGroup("테스트그룹")));
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenu(menu);

        Product product = ProductTestSupport.createProduct("치킨", BigDecimal.valueOf(20000));
        Product savedProduct = this.productRepository.save(product);
        menuProduct.setProduct(savedProduct);

        menuProduct.setQuantity(1);

        menu.addMenuProducts(this.menuProductRepository.save(menuProduct));
    }

}
