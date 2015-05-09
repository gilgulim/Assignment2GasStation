
package gasstation;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ObjectFilter implements Filter {

private Object filtered;
	
	public ObjectFilter(Object toFilter) {
		filtered = toFilter;
	}
	
	@Override
	public boolean isLoggable(LogRecord rec) {
		if (rec.getParameters() != null) {
			Object temp = rec.getParameters()[0];
			return filtered == temp;
		}
		else
			return false;
	}

}
