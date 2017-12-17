package pl.kkosinski.sentenceparser.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class Sentence {

	private final List<String> words;

	public Sentence(List<String> words) {
		this.words = words;
	}

	public List<String> getWords() {
		return words;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Sentence sentence = (Sentence) o;

		return new EqualsBuilder()
				.append(words, sentence.words)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(words)
				.toHashCode();
	}
}
