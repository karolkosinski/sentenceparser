package pl.kkosinski.sentenceparser.parser;

import java.io.Closeable;

public interface TextToSentenceParser extends Closeable {

	void parse();
}
