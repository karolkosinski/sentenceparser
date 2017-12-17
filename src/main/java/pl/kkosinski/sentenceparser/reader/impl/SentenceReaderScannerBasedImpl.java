package pl.kkosinski.sentenceparser.reader.impl;

import org.apache.commons.lang3.StringUtils;
import pl.kkosinski.sentenceparser.reader.SentenceReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SentenceReaderScannerBasedImpl implements SentenceReader {

	private static final String SENTENCE_ENDING_PATTERN = "[\\.\\?\\!]";
	private static final int SPLITERATOR_OPTIONS = Spliterator.ORDERED | Spliterator.NONNULL;

	private final Scanner scanner;

	public SentenceReaderScannerBasedImpl(InputStream inputStream) {
		scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
		scanner.useDelimiter(SENTENCE_ENDING_PATTERN);
	}

	@Override
	public Stream<String> sentences() {
		Iterator<String> sentenceIterator = new Iterator<String>() {
			@Override
			public boolean hasNext() {
				return scanner.hasNext();
			}

			@Override
			public String next() {
				return scanner.next();
			}
		};
		return iteratorToStream(sentenceIterator);
	}

	@Override
	public void close() throws IOException {
		scanner.close();
	}

	private Stream<String> iteratorToStream(Iterator<String> sentenceIterator) {
		return StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(sentenceIterator, SPLITERATOR_OPTIONS), false)
				.filter(StringUtils::isNotBlank);
	}
}
