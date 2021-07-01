package kitchenpos.fixture;

public class CleanUp {
    public static void cleanUpOrderFirst() {
        MenuGroupFixture.cleanUp();
        TableGroupFixture.cleanUp();
        ProductFixture.cleanUp();
        MenuFixture.cleanUp();
        OrderFixture.cleanUp();
        OrderTableFixture.cleanUp();
    }

    public static void cleanUpTableFirst() {
        MenuGroupFixture.cleanUp();
        TableGroupFixture.cleanUp();
        ProductFixture.cleanUp();
        MenuFixture.cleanUp();
        OrderTableFixture.cleanUp();
        OrderFixture.cleanUp();
    }
}
