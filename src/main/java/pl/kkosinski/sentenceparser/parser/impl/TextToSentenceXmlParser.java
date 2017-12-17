package pl.kkosinski.sentenceparser.parser.impl;

import org.apache.commons.text.StringEscapeUtils;
import pl.kkosinski.sentenceparser.exception.SentenceParserException;
import pl.kkosinski.sentenceparser.model.Sentence;
import pl.kkosinski.sentenceparser.parser.TextToSentenceParser;
import pl.kkosinski.sentenceparser.reader.SentenceReader;
import pl.kkosinski.sentenceparser.reader.impl.SentenceReaderScannerBasedImpl;
import pl.kkosinski.sentenceparser.util.mapper.SentenceMapper;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.List;

final class TextToSentenceXmlParser implements TextToSentenceParser {

	private static final String ROOT_ELEMENT_NAME = "text";
	private static final String SENTENCE_ELEMENT_NAME = "sentence";
	private static final String WORD_ELEMENT_NAME = "word";

	private final SentenceReader sentenceReader;

	private final XMLStreamWriter outputXmlWriter;

	TextToSentenceXmlParser(InputStream readFrom, OutputStream writeTo) {
		this.sentenceReader = prepareSentenceReader(readFrom);
		this.outputXmlWriter = prepareXmlWriter(writeTo);
	}

	@Override
	public void parse() {
		try {
			writeDocumentContent();
		} catch (XMLStreamException e) {
			throw new SentenceParserException("An error occurred during writing sentence to xml file", e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			sentenceReader.close();
			outputXmlWriter.close();
		} catch (XMLStreamException e) {
			throw new IOException(e);
		}
	}

	private void writeDocumentContent() throws XMLStreamException {
		writeDocumentHeader();
		outputXmlWriter.writeStartElement(ROOT_ELEMENT_NAME);
		outputXmlWriter.writeCharacters(String.format("%n"));
		sentenceReader.sentences().map(SentenceMapper::getWithWordsSortedAscending).forEach(this::writeSentence);
		outputXmlWriter.writeEndElement();
	}

	private void writeDocumentHeader() throws XMLStreamException {
		outputXmlWriter.writeStartDocument("utf-8", "1.0");
		outputXmlWriter.writeCharacters(String.format("%n"));
	}

	private void writeSentence(Sentence sentence) {
		try {
			outputXmlWriter.writeStartElement(SENTENCE_ELEMENT_NAME);
			writeWords(sentence.getWords());
			outputXmlWriter.writeEndElement();
			outputXmlWriter.writeCharacters(String.format("%n"));
		} catch (Exception e) {
			throw new SentenceParserException("An error occurred during writing sentence to xml file", e);
		}
	}

	private void writeWords(List<String> words) throws XMLStreamException {
		for (String word : words) {
			writeWord(word);
		}
	}

	private void writeWord(String word) throws XMLStreamException {
		outputXmlWriter.writeStartElement(WORD_ELEMENT_NAME);
		outputXmlWriter.writeCharacters(StringEscapeUtils.escapeXml10(word));
		outputXmlWriter.writeEndElement();
	}

	private SentenceReaderScannerBasedImpl prepareSentenceReader(InputStream readFrom) {
		return new SentenceReaderScannerBasedImpl(readFrom);
	}

	private XMLStreamWriter prepareXmlWriter(OutputStream out) {
		XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
		try {
			return outputFactory.createXMLStreamWriter(bufferedWriter);
		} catch (XMLStreamException e) {
			throw new SentenceParserException("Xml stream writer cannot be created", e);
		}
	}
}
