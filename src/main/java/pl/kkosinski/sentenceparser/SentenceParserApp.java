package pl.kkosinski.sentenceparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kkosinski.sentenceparser.exception.SentenceParserException;
import pl.kkosinski.sentenceparser.parser.TextToSentenceParser;
import pl.kkosinski.sentenceparser.parser.impl.TextToSentenceParserFactory;

import java.util.EnumMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SentenceParserApp {

	private static final Logger log = LoggerFactory.getLogger(SentenceParserApp.class);

	private static final AppMode DEFAULT_MODE = AppMode.XML;

	private static final EnumMap<AppMode, Supplier<TextToSentenceParser>> appModeStrategies;

	static {
		appModeStrategies = new EnumMap<>(AppMode.class);
		appModeStrategies.put(AppMode.XML, () -> TextToSentenceParserFactory.getSentenceToXmlParser(System.in, System.out));
		appModeStrategies.put(AppMode.CSV, () -> TextToSentenceParserFactory.getSentenceToCsvParser(System.in, System.out));
	}

	public static void main(String[] args) {
		long operationStartTime = System.currentTimeMillis();
		try {
			performTextParsing(chooseAppMode(args));
		} catch (Exception e) {
			log.error("", e);
		}
		long operationEndTime = System.currentTimeMillis();
		log.info("Execution time: {} seconds", calculateDuration(operationStartTime, operationEndTime));
	}

	private static AppMode chooseAppMode(String[] args) {
		AppMode appMode;
		if (isApplicationArgumentsListEmpty(args)) {
			appMode = DEFAULT_MODE;
			log.info("Application running mode not specified. Using default {} mode", DEFAULT_MODE);
		} else {
			appMode = AppMode.parse(args[0]);
			log.info("Application is running in {} mode", appMode);
		}
		return appMode;
	}

	private static boolean isApplicationArgumentsListEmpty(String[] args) {
		return args.length == 0;
	}

	private static void performTextParsing(AppMode appMode) {
		try (TextToSentenceParser parser = appModeStrategies.get(appMode).get()) {
			parser.parse();
		} catch (Exception e) {
			throw new SentenceParserException("", e);
		}
	}

	private static long calculateDuration(long operationStartTime, long operationEndTime) {
		return TimeUnit.MILLISECONDS.toSeconds(operationEndTime - operationStartTime);
	}
}
