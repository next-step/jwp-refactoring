package kitchenpos.resource;

public enum UriResource {
    상품_API("/api/products"),
    메뉴_API("/api/menus"),
    메뉴_그룹_API("/api/menu-groups"),
    ;

    private final String uri;

    UriResource(String uri) {
        this.uri = uri;
    }

    public String uri() {
        return uri;
    }
}
