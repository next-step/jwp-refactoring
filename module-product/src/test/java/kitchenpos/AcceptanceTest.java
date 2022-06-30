package kitchenpos;

import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;

import io.restassured.RestAssured;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.helper.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    public ProductResponse 후라이드;
    public ProductResponse 양념;

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
     * 상품 후라이드 : 17000원 양념 : 15000원
     */
    public void init() {
        상품_설정하기();
    }


    private void 상품_설정하기() {
        후라이드 = 상품_등록하기("후라이드", 17000).as(ProductResponse.class);
        양념 = 상품_등록하기("양념", 15000).as(ProductResponse.class);
    }

}
