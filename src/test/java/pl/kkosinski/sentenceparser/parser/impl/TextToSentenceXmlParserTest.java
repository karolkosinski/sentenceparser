package pl.kkosinski.sentenceparser.parser.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;
import pl.kkosinski.sentenceparser.parser.TextToSentenceParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TextToSentenceXmlParserTest {

	@Test
	public void parseTest() throws Exception {
		//given
		String expectedXmlDocument = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"<text>\n" +
				"<sentence><word>aa</word><word>bb</word></sentence>\n" +
				"<sentence><word>cc</word><word>dd</word></sentence>\n" +
				"</text>";

		String inputText = "bb aa. dd cc.";
		InputStream in = convert(inputText);
		OutputStream out = prepareOutputForTesting();

		TextToSentenceParser xmlParser = TextToSentenceParserFactory.getSentenceToXmlParser(in, out);
		//when
		xmlParser.parse();
		xmlParser.close();
		//then
		Assert.assertEquals(expectedXmlDocument, out.toString());
	}

	private InputStream convert(String str) throws Exception {
		return IOUtils.toInputStream(str, StandardCharsets.UTF_8.name());
	}

	private OutputStream prepareOutputForTesting() {
		return new ByteArrayOutputStream();
	}

}