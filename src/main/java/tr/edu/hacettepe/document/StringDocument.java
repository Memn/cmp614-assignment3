package tr.edu.hacettepe.document;

import java.io.Reader;
import java.io.StringReader;

public class StringDocument implements Document {

	private final String string;

	@SuppressWarnings("WeakerAccess")
    public StringDocument(String str) {
		this.string = str;
	}

	@Override
	public Reader openReader() {
		return new StringReader(string);
	}


}
