package glycon.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestURL {

	private static final String HTTPS_API_ADVISERINFO_SEC_GOV = "https://api.adviserinfo.sec.gov/search";

	private static final String HTTPS_API_BROKERCHECK_FINRA_ORG = "https://api.brokercheck.finra.org/search";

	public static final int AGRESSIVE_TIME_PER_REQUEST = 250;

	private static final int CONNECTION_TIMEOUT = 10000;

	public static final int DEV_TIME_PER_REQUEST = 1000;

	private static final String ERROR_STRING_LITERAL = "NULL";

	private static final String MASKED_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

	public static final int NASTY_TIME_PER_REQUEST = 250;

	public static final int NORMAL_TIME_PER_REQUEST = 2000;

	public static final int POLITE_TIME_PER_REQUEST = 4000;

	private static final String POLITE_USER_AGENT = "glycon-crawler @ Southampton University (ms2u19@soton.ac.uk)";

	public static String getAdviserinfoJSON(String managerCRD) {

		String url = HTTPS_API_ADVISERINFO_SEC_GOV + "/individual/" + managerCRD
				+ "?hl=true&nrows=12&query=&start=0&wt=json";

		return sendResponse(url, MASKED_USER_AGENT, DEV_TIME_PER_REQUEST);
	}

	public static String getFirmJSON(String unfriendlyFirmName) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/firm?hl=true&json.wrf=angular.callbacks._0&nrows=12&query="
				+ unfriendlyFirmName + "&r=25&sort=score+desc&wt=json";

		return sendResponse(url, POLITE_USER_AGENT, DEV_TIME_PER_REQUEST);
	}

	public static String getFirmManagersByExperienceJSON(int firmID, String experienceBoundry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG
				+ "/individual?filter=broker%3Dtrue,ia%3Dtrue,brokeria%3Dtrue,active%3Dtrue,prev%3Dtrue,bar%3Dtrue,experience%3D"
				+ experienceBoundry + "&firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._s&nrows=12&r=25&sort=score+desc&start=0&wt=json";

		return sendResponse(url, POLITE_USER_AGENT, DEV_TIME_PER_REQUEST);
	}

	public static String getFirmManagersByRangeAndAlphabeticalJSON(String firmCRD, int entries, int startEntry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual?firm=" + firmCRD
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._0&nrows=" + entries
				+ "&r=25&sort=score+desc&start=" + startEntry + "&wt=json";

		return sendResponse(url, POLITE_USER_AGENT, DEV_TIME_PER_REQUEST);

	}

	public static String getFirmManagersByRangeAndExpirienceJSON(String firmID, int entries, int startEntry,
			int startExperienceBoundry, String endExperienceBoundry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG
				+ "/individual?filter=broker%3Dtrue,ia%3Dtrue,brokeria%3Dtrue,active%3Dtrue,prev%3Dtrue,bar%3Dtrue,experience%3D"
				+ startExperienceBoundry + "-" + endExperienceBoundry + "&firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._s&nrows=" + entries
				+ "&r=25&sort=bc_lastname_sort+asc,bc_firstname_sort+asc,bc_middlename_sort+asc,score+desc&start="
				+ startEntry + "&wt=json";

		return sendResponse(url, POLITE_USER_AGENT, DEV_TIME_PER_REQUEST);

	}

	public static String getFirmManagersByRangeJSON(String firmID, int entries, int startEntry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual?firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._0&nrows=" + entries
				+ "&r=25&sort=bc_lastname_sort+asc,bc_firstname_sort+asc,bc_middlename_sort+asc,score+desc&start="
				+ startEntry + "&wt=json";

		return sendResponse(url, POLITE_USER_AGENT, DEV_TIME_PER_REQUEST);
	}

	public static String getManagerFinraJSON(String managerCRD) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual/" + managerCRD
				+ "?json.wrf=angular.callbacks._0&wt=json";

		return sendResponse(url, POLITE_USER_AGENT, DEV_TIME_PER_REQUEST);
	}

	public static String probeFirmHits(String firmID) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual?firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._0&nrows=0&r=25&sort=score+desc&wt=json";

		return sendResponse(url, POLITE_USER_AGENT, DEV_TIME_PER_REQUEST);
	}

	private static String sendResponse(String url, String userAgent, int requestTime) {

		URL obj;
		StringBuilder response = null;

		try {

			Thread.sleep(requestTime);

			obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// Set timeout
			con.setConnectTimeout(CONNECTION_TIMEOUT);

			// add request header
			con.setRequestProperty("User-Agent", userAgent);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();

		} catch (IOException e) {

			return ERROR_STRING_LITERAL;

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();

			return ERROR_STRING_LITERAL;

		}

		return response.toString();

	}

	private RequestURL() {
		throw new IllegalStateException("Utility class");
	}

}
