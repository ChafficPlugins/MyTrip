package de.chaffic.MyTrip.API.Objects;

public enum Key {
    DRUGSET("2e116c45-8bd6-4297-a8c1-98041c08d39c"),
    DRUTEST("764d1358-32d9-4f8b-af6c-c5d64de2bfd0"),
    ANTITOXIN("8000f544-c0db-4af2-aea5-80fa8bc53aaa");

    public final String uuid;

    Key(String uuid) {
        this.uuid = uuid;
    }

    public String key() {
        return uuid + "drug_tool";
    }
}
