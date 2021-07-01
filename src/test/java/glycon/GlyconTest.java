package glycon;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GlyconTest {

	@Test
	void test() {

		List<String> rawFrimList = new ArrayList<>();

		rawFrimList.add("361");

		GlyconSystemYielder.workOnFirmDisclosureList(rawFrimList, 8);

	}

}
