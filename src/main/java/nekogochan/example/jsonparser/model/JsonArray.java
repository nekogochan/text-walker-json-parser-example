package nekogochan.example.jsonparser.model;

import java.util.List;
import java.util.StringJoiner;

public record JsonArray(List<JsonNode> values) implements JsonNode {
  @Override
  public String toRawJson() {
    var s = new StringJoiner(",", "[", "]");
    values.forEach((v) -> s.add(v.toRawJson()));
    return s.toString();
  }
}
