package pl.kkosinski.sentenceparser.parser.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import pl.kkosinski.sentenceparser.model.Sentence;
import pl.kkosinski.sentenceparser.parser.TextToSentenceParser;
import pl.kkosinski.sentenceparser.reader.SentenceReader;
import pl.kkosinski.sentenceparser.reader.impl.SentenceReaderScannerBasedImpl;
import pl.kkosinski.sentenceparser.util.mapper.SentenceMapper;

import java.io.*;
import java.util.StringJoiner;

final class TextToSentenceCsvParser implements TextToSentenceParser {

	private static final String CSV_TEMP_FILE_NAME = "csv-lines";
	private static final String CSV_TEMP_FILE_SUFFIX = "tmp";

	private static final String SEPARATOR = ", ";
	private static final String ROW_HEADER_PREFIX = "Sentence ";
	private static final String COLUMN_HEADER_PREFIX = "Word ";

	private final SentenceReader sentenceReader;

	private final Writer outputCsvWriter;

	private final OutputStream outCsvStream;

	private class CsvLinesTemporaryFileInfo {
		private int longestSentenceLength;

		private File temporaryFile;

		private void updateLongestLineLength(Sentence sentence) {
			if (sentence.getWords().size() > longestSentenceLength) {
				longestSentenceLength = sentence.getWords().size();
			}
		}
	}

	private class SentenceCounter {
		private long count;

		private String getNextRowHeader() {
			return ROW_HEADER_PREFIX + ++count;
		}
	}

	TextToSentenceCsvParser(InputStream readFrom, OutputStream writeTo) {
		this.sentenceReader = prepareSentenceReader(readFrom);
		this.outputCsvWriter = prepareWriter(writeTo);
		this.outCsvStream = writeTo;
	}

	private SentenceReaderScannerBasedImpl prepareSentenceReader(InputStream readFrom) {
		return new SentenceReaderScannerBasedImpl(readFrom);
	}

	private Writer prepareWriter(OutputStream out) {
		return new BufferedWriter(new OutputStreamWriter(out));
	}

	@Override
	public void parse() {
		try {
			CsvLinesTemporaryFileInfo csvLinesTemporaryFileInfo = parseToCsvStoringInTemporaryFile();
			writeCsvHeaderToOutputCsv(csvLinesTemporaryFileInfo);
			moveCsvLinesFromTemporaryFileToOutputCsv(csvLinesTemporaryFileInfo);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void close() throws IOException {
		sentenceReader.close();
		outputCsvWriter.close();
	}

	private CsvLinesTemporaryFileInfo parseToCsvStoringInTemporaryFile() throws IOException {
		SentenceCounter counter = new SentenceCounter();
		CsvLinesTemporaryFileInfo temporaryOutputFileInfo = new CsvLinesTemporaryFileInfo();
		File tmp = File.createTempFile(CSV_TEMP_FILE_NAME, CSV_TEMP_FILE_SUFFIX);
		try (
				Writer writer = new BufferedWriter(new FileWriter(tmp))
		) {
			sentenceReader.sentences().map(SentenceMapper::getWithWordsSortedAscending).forEach(s -> {
				write(writer, getCsvLine(counter.getNextRowHeader(), s));
				temporaryOutputFileInfo.updateLongestLineLength(s);
			});
		}
		temporaryOutputFileInfo.temporaryFile = tmp;
		return temporaryOutputFileInfo;
	}

	private void writeCsvHeaderToOutputCsv(CsvLinesTemporaryFileInfo csvLinesTemporaryFileInfo) throws IOException {
		outputCsvWriter.write(getCsvHeader(csvLinesTemporaryFileInfo));
		outputCsvWriter.flush();
	}

	private String getCsvHeader(CsvLinesTemporaryFileInfo csvFileInfo) {
		StringJoiner headerJoiner = new StringJoiner(SEPARATOR);
		headerJoiner.add(StringUtils.EMPTY);
		for (int i = 1; i <= csvFileInfo.longestSentenceLength; i++) {
			headerJoiner.add(COLUMN_HEADER_PREFIX + i);
		}
		return String.format("%s%n", headerJoiner.toString());
	}

	private void moveCsvLinesFromTemporaryFileToOutputCsv(CsvLinesTemporaryFileInfo csvLinesTemporaryFileInfo) throws IOException {
		FileUtils.copyFile(csvLinesTemporaryFileInfo.temporaryFile, outCsvStream);
		FileUtils.deleteQuietly(csvLinesTemporaryFileInfo.temporaryFile);
	}

	private String getCsvLine(String rowHeader, Sentence s) {
		StringJoiner lineJoiner = new StringJoiner(SEPARATOR);
		lineJoiner.add(rowHeader);
		for (String word : s.getWords()) {
			lineJoiner.add(StringEscapeUtils.escapeCsv(word));
		}
		return String.format("%s%n", lineJoiner.toString());
	}

	private void write(Writer writer, String line) {
		try {
			writer.write(line);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
