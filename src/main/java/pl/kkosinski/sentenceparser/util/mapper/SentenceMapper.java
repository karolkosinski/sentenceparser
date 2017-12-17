package pl.kkosinski.sentenceparser.util.mapper;

import org.apache.commons.lang3.StringUtils;
import pl.kkosinski.sentenceparser.model.Sentence;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SentenceMapper {

	private static final String WHITESPACE_OR_WORD_DIVIDING_PUNCTUATION = "[\\s,;:!?-]";

	private SentenceMapper() {
	}

	public static Sentence getWithWordsSortedAscending(String str) {
		List<String> words = Stream.of(str.split(WHITESPACE_OR_WORD_DIVIDING_PUNCTUATION))
				.filter(StringUtils::isNotBlank)
				.sorted(String::compareToIgnoreCase)
				.collect(Collectors.toList());
		return new Sentence(words);
	}


}
