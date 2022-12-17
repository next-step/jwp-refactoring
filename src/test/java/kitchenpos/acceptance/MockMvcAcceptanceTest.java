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
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableRequest;
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

    ResultActions 테이블_그룹_생성_요청(TableRequest... tables) throws Exception {
        return mockPost("/api/table-groups", new TableGroupRequest(Arrays.asList(tables)));
    }

    ResultActions 테이블_생성_요청(int person, boolean empty) throws Exception {
        return mockPost("/api/tables", new OrderTableRequest(person, empty));
    }

    OrderTableResponse 테이블_생성(int person, boolean empty) throws Exception {
        ResultActions 테이블_생성_결과 = 테이블_생성_요청(person, empty);
        return getObjectByResponse(테이블_생성_결과, OrderTableResponse.class);
    }

    TableGroupResponse 테이블_그룹_생성(TableRequest... tables) throws Exception {
        ResultActions 테이블_그룹_생성_결과 = 테이블_그룹_생성_요청(tables);
        return getObjectByResponse(테이블_그룹_생성_결과, TableGroupResponse.class);
    }

    ResultActions 상품_등록_요청(String name, Integer price) throws Exception {
        return mockPost("/api/products", new ProductRequest(name, new BigDecimal(price)));
    }

    Product 상품_등록(String name, Integer price) throws Exception {
        ResultActions response = 상품_등록_요청(name, price);
        return getObjectByResponse(response, Product.class);
    }

    MenuResponse 메뉴_등록(String menuName, Integer price, MenuGroup group, List<MenuProduct> products) throws Exception {
        ResultActions response = 메뉴_등록_요청(menuName, price, group, products);
        return getObjectByResponse(response, MenuResponse.class);
    }

    ResultActions 메뉴_등록_요청(String menuName, Integer price, MenuGroup group, List<MenuProduct> products) throws Exception {
        List<MenuProductRequest> menuProductRequests = products.stream()
                .map(
                        menuProduct -> new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity())
                ).collect(Collectors.toList());
        MenuRequest menuRequest = new MenuRequest(menuName, new BigDecimal(price), group.getId(), menuProductRequests);
        return mockPost("/api/menus", menuRequest);
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

    ResultActions 주문_요청(OrderTableResponse orderTable, MenuResponse menu) throws Exception {
        OrderRequest order = new OrderRequest(orderTable.getId(),
                Collections.singletonList(new OrderLineItemRequest(menu.getId(), 1)));
        return mockPost("/api/orders", order);
    }

    OrderResponse 주문_생성(OrderTableResponse orderTable, MenuResponse menu) throws Exception {
        return getObjectByResponse(주문_요청(orderTable, menu), OrderResponse.class);
    }
}
