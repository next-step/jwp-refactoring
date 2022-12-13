package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext ctx;

    @BeforeEach
    void beforeEach(){
        this.mockMvc = webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }


    ResultActions mockGet(String url) throws Exception{
        return mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
        );
    }

    ResultActions mockPost(String url, Object body) throws Exception{
        String content = objectMapper.writeValueAsString(body);
        return mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(content)
        );
    }

    ResultActions mockPut(String url, Long targetId, Object body) throws Exception{
        String content = objectMapper.writeValueAsString(body);
        return mockMvc.perform(put(url, targetId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(content)
        );
    }

    ResultActions mockDelete(String url, Long targetId) throws Exception{
        return mockMvc.perform(delete(url, targetId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
        );
    }

    <T> T getObjectByResponse(ResultActions resultActions, Class<T> clazz)
            throws JsonProcessingException, UnsupportedEncodingException {
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, clazz);
    }

    ResultActions 테이블_그룹_제거_요청(Long id) throws Exception {
        return mockDelete("/api/table-groups/{tableGroupId}", id);
    }

    ResultActions 테이블_그룹_생성_요청(OrderTable... tables) throws Exception {
        return mockPost("/api/table-groups", new TableGroup(Arrays.asList(tables)));
    }

    ResultActions 테이블_생성_요청(int person, boolean empty) throws Exception {
        return mockPost("/api/tables", new OrderTable(person, empty));
    }

    OrderTable 테이블_생성(int person, boolean empty) throws Exception {
        ResultActions 테이블_생성_결과 = 테이블_생성_요청(person, empty);
        return getObjectByResponse(테이블_생성_결과, OrderTable.class);
    }

    TableGroup 테이블_그룹_생성(OrderTable... tables) throws Exception {
        ResultActions 테이블_그룹_생성_결과 = 테이블_그룹_생성_요청(tables);
        return getObjectByResponse(테이블_그룹_생성_결과, TableGroup.class);
    }

    Product 상품_등록(String name, Integer price) throws Exception {
        ResultActions response = mockPost("/api/products", new Product(name, new BigDecimal(price)));
        return getObjectByResponse(response, Product.class);
    }

    Menu 메뉴_등록(String menuName, Integer price, MenuGroup group, List<MenuProduct> products) throws Exception {
        Menu menu = new Menu(menuName, new BigDecimal(price), group.getId(), products);
        return getObjectByResponse(mockPost("/api/menus", menu), Menu.class);
    }

    ResultActions 메뉴_등록_요청(String menuName, Integer price, MenuGroup group, List<MenuProduct> products) throws Exception {
        Menu menu = new Menu(menuName, new BigDecimal(price), group.getId(), products);
        return mockPost("/api/menus", menu);
    }

    ResultActions 메뉴_전체_조회() throws Exception {
        return mockGet("/api/menus");
    }

    ResultActions 메뉴_그룹_전체_조회_요청() throws Exception {
        return mockGet("/api/menu-groups");
    }

    MenuGroup 메뉴_그룹_추가(String name) throws Exception {
        return getObjectByResponse(메뉴_그룹_추가_요청(name), MenuGroup.class);
    }

    ResultActions 메뉴_그룹_추가_요청(String name) throws Exception {
        return mockPost("/api/menu-groups", new MenuGroup(name));
    }

    ResultActions 주문_요청(OrderTable orderTable, Menu menu) throws Exception {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu.getId(), 1)));
        return mockPost("/api/orders", order);
    }

    Order 주문_생성(OrderTable orderTable, Menu menu) throws Exception {
        return getObjectByResponse(주문_요청(orderTable, menu), Order.class);
    }
}
