package nekogochan.example.jsonparser.exception;

import nekogochan.textwalker.charautomate.CharAutomate;

public class InvalidJsonArrayException extends InvalidJsonException {
  public InvalidJsonArrayException(CharAutomate auto, String comment) {
    super(auto, comment, "array");
  }
}
