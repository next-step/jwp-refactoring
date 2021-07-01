package kitchenpos.fixture;

public class CleanUp {
    public static void cleanUpOrderFirst() {
        TableGroupFixture.cleanUp();
        ProductFixture.cleanUp();
        MenuFixture.cleanUp();
        OrderFixture.cleanUp();
        OrderTableFixture.cleanUp();
    }

    public static void cleanUpTableFirst() {
        TableGroupFixture.cleanUp();
        ProductFixture.cleanUp();
        MenuFixture.cleanUp();
        OrderTableFixture.cleanUp();
        OrderFixture.cleanUp();
    }
}
