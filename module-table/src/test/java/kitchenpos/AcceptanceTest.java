package kitchenpos;

import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_추가하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.유휴테이블_여부_설정하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.테이블_손님_인원_설정하기;

import io.restassured.RestAssured;
import java.util.Arrays;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.helper.AcceptanceApiHelper.TableApiHelper;
import kitchenpos.helper.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    public static final String 먹는중 = "MEAL";
    public static final String 요리중 = "COOKING";
    public static final String 비어있음 = "true";
    public static final String 사용중 = "false";
    public OrderTableResponse 테이블_1;
    public OrderTableResponse 테이블_2;
    public OrderTableResponse 빈테이블_1;
    public OrderTableResponse 빈테이블_2;
    public MenuResponse 양념두마리_메뉴;
    public MenuResponse 양념세마리_메뉴;
    public MenuResponse 반반_메뉴;
    public OrderLineItemDTO 주문;
    public ProductResponse 후라이드;
    public ProductResponse 양념;
    public MenuGroupResponse 두마리메뉴;
    public MenuGroupResponse 반반메뉴;
    public MenuGroupResponse 세마리메뉴;
    @LocalServerPort
    int port;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    /**
     * 테이블 테이블_1 : 사용중 , 2명 테이블_2 : 사용중 , 3명 빈테이블_1 : 미사용중, 0명 빈테이블_2 : 미사용중, 0명 메뉴그룹 두마리메뉴 세마리메뉴
     * 반반메뉴 상품 후라이드 : 17000원 양념 : 15000원 메뉴 양념두마리메뉴 : 2222원, 두마리메뉴(그룹명), 양념한마리 양념세마리메뉴 : 3333원,
     * 세마리메뉴(그룹명), 양념세마리 반반메뉴 : 1111원, 반반메뉴(그룹명), 양념한마리&후라이드한마리 주문 양념두마리메뉴,2개
     */
    public void init() {
        테이블_설정하기();
        메뉴그룹_설정하기();
        상품_설정하기();
        메뉴_설정하기();
        주문_설정하기();
    }

    private void 메뉴그룹_설정하기() {
        두마리메뉴 = 메뉴그룹_등록하기("두마리메뉴").as(MenuGroupResponse.class);
        세마리메뉴 = 메뉴그룹_등록하기("세마리메뉴").as(MenuGroupResponse.class);
        반반메뉴 = 메뉴그룹_등록하기("반반메뉴").as(MenuGroupResponse.class);
    }


    private void 상품_설정하기() {
        후라이드 = 상품_등록하기("후라이드", 17000).as(ProductResponse.class);
        양념 = 상품_등록하기("양념", 15000).as(ProductResponse.class);
    }

    private void 주문_설정하기() {
        주문 = new OrderLineItemDTO();
        주문.setMenuId(양념두마리_메뉴.getId());
        주문.setQuantity(2L);
    }

    private void 메뉴_설정하기() {
        MenuProductDTO 양념_한마리 = new MenuProductDTO();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(1L);

        MenuProductDTO 양념_세마리 = new MenuProductDTO();
        양념_세마리.setProductId(양념.getId());
        양념_세마리.setQuantity(3L);

        MenuProductDTO 후라이드_한마리 = new MenuProductDTO();
        후라이드_한마리.setProductId(후라이드.getId());
        후라이드_한마리.setQuantity(1L);

        양념두마리_메뉴 = 메뉴_추가하기("양념두마리", 2222, 두마리메뉴.getId(), Arrays.asList(양념_한마리)).as(
            MenuResponse.class);
        양념세마리_메뉴 = 메뉴_추가하기("양념세마리", 3333, 세마리메뉴.getId(), Arrays.asList(양념_세마리)).as(
            MenuResponse.class);
        반반_메뉴 = 메뉴_추가하기("양념후라이드", 1111, 두마리메뉴.getId(), Arrays.asList(양념_한마리, 후라이드_한마리)).as(
            MenuResponse.class);
    }

    private void 테이블_설정하기() {
        테이블_1 = TableApiHelper.빈테이블_생성하기().as(OrderTableResponse.class);
        테이블_2 = TableApiHelper.빈테이블_생성하기().as(OrderTableResponse.class);
        빈테이블_1 = TableApiHelper.빈테이블_생성하기().as(OrderTableResponse.class);
        빈테이블_2 = TableApiHelper.빈테이블_생성하기().as(OrderTableResponse.class);

        유휴테이블_여부_설정하기("false", 테이블_1.getId());
        유휴테이블_여부_설정하기("false", 테이블_2.getId());
        테이블_손님_인원_설정하기(2, 테이블_1.getId());
        테이블_손님_인원_설정하기(3, 테이블_2.getId());
    }
}
