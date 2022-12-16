package kitchenpos.resource;

public enum UriResource {
    상품_API("/api/products"),
    메뉴_API("/api/menus"),
    메뉴_그룹_API("/api/menu-groups"),
    주문_테이블_API("/api/tables"),
    주문_테이블_그룹_API("/api/table-groups"),
    ;

    private final String uri;

    UriResource(String uri) {
        this.uri = uri;
    }

    public String uri() {
        return uri;
    }
}
