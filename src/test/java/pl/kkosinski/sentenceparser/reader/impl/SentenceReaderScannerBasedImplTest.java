package pl.kkosinski.sentenceparser.reader.impl;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import pl.kkosinski.sentenceparser.reader.SentenceReader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class SentenceReaderScannerBasedImplTest {

	@Test
	public void readSentenceTest() throws Exception {
		//given
		String inputText = "Sentence1.";
		String expectedRetrievedSentenceContent = "Sentence1";
		//when
		SentenceReader reader = new SentenceReaderScannerBasedImpl(convert(inputText));
		List<String> retrievedSentences = retrieveSentences(reader);
		//then
		Assert.assertEquals(expectedRetrievedSentenceContent, retrievedSentences.get(0));
	}

	@Test
	public void readManySentencesTest() throws Exception {
		//given
		String inputText = "Sentence1. Sentence2! Sentence3?";
		int sentenceCount = 3;
		//when
		SentenceReader reader = new SentenceReaderScannerBasedImpl(convert(inputText));
		List<String> retrievedSentences = retrieveSentences(reader);
		//then
		Assert.assertEquals(sentenceCount, retrievedSentences.size());
	}

	@Test
	public void readOneSentenceInMultipleLinesTest() throws Exception {
		//given
		String inputText = "Test \n test \n test.";
		//when
		SentenceReader reader = new SentenceReaderScannerBasedImpl(convert(inputText));
		List<String> retrievedSentences = retrieveSentences(reader);
		//then
		Assert.assertEquals(1, retrievedSentences.size());
	}

	@Test
	public void readTextNotDividedIntoSentencesTest() throws Exception {
		//given
		String inputText = "Test test test";
		//when
		SentenceReader reader = new SentenceReaderScannerBasedImpl(convert(inputText));
		List<String> retrievedSentences = retrieveSentences(reader);
		//then
		Assert.assertEquals(inputText, retrievedSentences.get(0));
	}

	private InputStream convert(String str) throws Exception {
		return IOUtils.toInputStream(str, StandardCharsets.UTF_8.name());
	}

	private List<String> retrieveSentences(SentenceReader reader) {
		return reader.sentences().collect(Collectors.toList());
	}
}