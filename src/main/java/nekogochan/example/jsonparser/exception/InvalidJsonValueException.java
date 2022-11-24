package nekogochan.example.jsonparser.exception;

import nekogochan.textwalker.charautomate.CharAutomate;

public class InvalidJsonValueException extends InvalidJsonException {
  public InvalidJsonValueException(CharAutomate auto, String comment) {
    super(auto, comment, "value");
  }
}
