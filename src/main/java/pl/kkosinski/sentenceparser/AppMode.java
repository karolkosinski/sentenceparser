package pl.kkosinski.sentenceparser;

import pl.kkosinski.sentenceparser.exception.ApplicationConfigurationException;

public enum AppMode {
	XML, CSV;

	public static AppMode parse(String mode) {
		for (AppMode appMode : AppMode.values()) {
			if (appMode.toString().equalsIgnoreCase(mode)) {
				return appMode;
			}
		}
		throw new ApplicationConfigurationException("Unsupported application mode supplied. Please use xml or csv");
	}
}
