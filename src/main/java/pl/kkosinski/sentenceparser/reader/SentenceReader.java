package pl.kkosinski.sentenceparser.reader;

import java.io.Closeable;
import java.util.stream.Stream;

public interface SentenceReader extends Closeable {

	Stream<String> sentences();
}
