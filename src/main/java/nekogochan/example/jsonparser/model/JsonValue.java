package nekogochan.example.jsonparser.model;

public record JsonValue(Object value) implements JsonNode {

  public String asStr() {
    return (String) value;
  }

  public int asInt() {
    return (int) value;
  }

  public double asDouble() {
    return (double) value;
  }

  public boolean asBoolean() {
    return (boolean) value;
  }

  @Override
  public String toRawJson() {
    if (value instanceof String s) {
      return '"' + s + '"';
    }
    return String.valueOf(value);
  }
}
