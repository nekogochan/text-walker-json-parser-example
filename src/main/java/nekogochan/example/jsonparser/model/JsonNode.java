package nekogochan.example.jsonparser.model;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public sealed interface JsonNode permits JsonArray, JsonObject, JsonValue {
  default List<JsonNode> asArr() {
    return ((JsonArray) this).values();
  }

  default Map<String, JsonNode> asObj() {
    return ((JsonObject) this).entries()
                              .stream()
                              .collect(toMap(
                                JsonObject.Entry::key,
                                JsonObject.Entry::value
                              ));
  }

  default Object asVal() {
    return ((JsonValue) this).value();
  }

  @SuppressWarnings("RedundantCast")
  default String asStr() {
    return ((JsonValue) this).asStr();
  }

  @SuppressWarnings("RedundantCast")
  default int asInt() {
    return ((JsonValue) this).asInt();
  }

  @SuppressWarnings("RedundantCast")
  default double asDouble() {
    return ((JsonValue) this).asDouble();
  }

  @SuppressWarnings("RedundantCast")
  default boolean asBoolean() {
    return ((JsonValue) this).asBoolean();
  }

  String toRawJson();
}
