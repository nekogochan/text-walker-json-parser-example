package nekogochan.example.jsonparser.model;

import java.util.List;
import java.util.stream.Collectors;

public record JsonObject(List<Entry> entries) implements JsonNode {
  public JsonNode get(String key) {
    for (var e : entries) {
      if (e.key.equals(key)) {
        return e.value;
      }
    }
    return null;
  }

  @Override
  public String toRawJson() {
    return entries.stream()
                  .map(Entry::toRawJson)
                  .collect(Collectors.joining(",", "{", "}"));
  }

  public record Entry(String key, JsonNode value) {
    String toRawJson() {
      return '"' + key + '"' + ':' + value.toRawJson();
    }
  }
}
