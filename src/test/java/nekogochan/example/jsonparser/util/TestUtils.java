package nekogochan.example.jsonparser.util;

import org.junit.jupiter.params.provider.Arguments;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public interface TestUtils {
  static Stream<Arguments> args(Map<?, ?> args) {
    return args(args, x -> x);
  }

  static <V> Stream<Arguments> args(Map<?, V> args, Function<V, ?> mapper) {
    return args.entrySet()
               .stream()
               .map(x -> new AbstractMap.SimpleEntry<>(x.getKey(), mapper.apply(x.getValue())))
               .map(x -> Arguments.of(x.getKey(), x.getValue()));
  }

  static Stream<Arguments> args(List<?> args) {
    return args.stream()
               .map(Arguments::of);
  }
}
