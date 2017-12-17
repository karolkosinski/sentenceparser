package pl.kkosinski.sentenceparser.parser.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;
import pl.kkosinski.sentenceparser.parser.TextToSentenceParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TextToSentenceCsvParserTest {
	@Test
	public void parseTest() throws Exception {
		//given
		String expectedCsvDocument =
				", Word 1, Word 2\n" +
						"Sentence 1, aa, bb\n" +
						"Sentence 2, cc, dd\n";

		String inputText = "bb aa. dd cc.";
		InputStream in = convert(inputText);
		OutputStream out = prepareOutputForTesting();

		TextToSentenceParser csvParser = TextToSentenceParserFactory.getSentenceToCsvParser(in, out);
		//when
		csvParser.parse();
		csvParser.close();
		//then
		Assert.assertEquals(expectedCsvDocument, out.toString());
	}

	private InputStream convert(String str) throws Exception {
		return IOUtils.toInputStream(str, StandardCharsets.UTF_8.name());
	}

	private OutputStream prepareOutputForTesting() {
		return new ByteArrayOutputStream();
	}

}