package pl.kkosinski.sentenceparser.parser.impl;

import pl.kkosinski.sentenceparser.parser.TextToSentenceParser;

import java.io.InputStream;
import java.io.OutputStream;

public final class TextToSentenceParserFactory {

	private TextToSentenceParserFactory() {
	}

	public static TextToSentenceParser getSentenceToXmlParser(InputStream readSentencesFrom, OutputStream writeXmlTo) {
		return new TextToSentenceXmlParser(readSentencesFrom, writeXmlTo);
	}

	public static TextToSentenceParser getSentenceToCsvParser(InputStream readSentencesFrom, OutputStream writeCsvTo) {
		return new TextToSentenceCsvParser(readSentencesFrom, writeCsvTo);
	}
}
