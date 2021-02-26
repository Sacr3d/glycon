package glycon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import glycon.utils.FileUtil;

class GlyconTest {

	@Test
	void test() {

		List<String> rawFrimList = null;

		try {
			rawFrimList = FileUtil.parseFirmsInTextFile("Firms.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Glycon.createBrokersWithDisclosuresList(new ArrayList<>(Arrays.asList("361")));

	}

}
