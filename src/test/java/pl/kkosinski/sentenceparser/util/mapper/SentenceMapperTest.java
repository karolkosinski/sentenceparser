package pl.kkosinski.sentenceparser.util.mapper;

import org.junit.Assert;
import org.junit.Test;
import pl.kkosinski.sentenceparser.model.Sentence;

public class SentenceMapperTest {

	@Test
	public void getWordsSortedAscendingTest() throws Exception {
		//given
		String sentenceToParse = "What\t he  \n shouted \n was shocking:  停在那儿, 你这肮脏的掠夺者";
		String[] wordsSortedAscending = {"he", "shocking", "shouted", "was", "What", "你这肮脏的掠夺者", "停在那儿"};
		//when
		Sentence result = SentenceMapper.getWithWordsSortedAscending(sentenceToParse);
		//then
		Assert.assertArrayEquals(wordsSortedAscending, result.getWords().toArray());
	}

	@Test
	public void getWordsPreservingNonWordDividingPunctuation() throws Exception {
		//given
		String sentenceToParse = "word1, _word2;:?!";
		String[] words = {"_word2", "word1"};
		//when
		Sentence result = SentenceMapper.getWithWordsSortedAscending(sentenceToParse);
		//then
		Assert.assertArrayEquals(words, result.getWords().toArray());
	}

	@Test
	public void getWordsSortedAscendingIgnoringCaseTest() throws Exception {
		//given
		String sentenceToParse = "bbb BBB AAA aaa";
		String[] wordsWithOrderNotChangedByCase = {"AAA", "aaa", "bbb", "BBB"};
		//when
		Sentence result = SentenceMapper.getWithWordsSortedAscending(sentenceToParse);
		//then
		Assert.assertArrayEquals(wordsWithOrderNotChangedByCase, result.getWords().toArray());
	}
}