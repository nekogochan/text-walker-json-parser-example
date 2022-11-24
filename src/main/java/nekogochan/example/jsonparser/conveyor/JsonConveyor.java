package nekogochan.example.jsonparser.conveyor;

import nekogochan.example.jsonparser.conveyor.nodes.JsonArrayConveyor;
import nekogochan.example.jsonparser.conveyor.nodes.JsonElementConveyor;
import nekogochan.example.jsonparser.conveyor.nodes.JsonObjectConveyor;
import nekogochan.example.jsonparser.conveyor.nodes.JsonValueConveyor;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

public class JsonConveyor {

  private JsonObjectConveyor forObject;
  private JsonArrayConveyor forArray;
  private JsonValueConveyor forValue;

  public JsonConveyor() {
    forObject = new JsonObjectConveyor(this);
    forArray = new JsonArrayConveyor(this);
    forValue = new JsonValueConveyor(this);
  }

  public JsonObjectConveyor forObject() {
    return forObject;
  }

  public JsonArrayConveyor forArray() {
    return forArray;
  }

  public JsonValueConveyor forValue() {
    return forValue;
  }

  public JsonElementConveyor<?> suitableConveyor(char ch) {
    if (isDigit(ch)) {
      return forValue;
    }
    if (isAlphabetic(ch)) {
      return forValue;
    }
    if (ch == '"') {
      return forValue;
    }
    if (ch == '{') {
      return forObject;
    }
    if (ch == '[') {
      return forArray;
    }
    return null;
  }
}
