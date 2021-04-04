package glycon.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestURL {

	private static final String HTTPS_API_ADVISERINFO_SEC_GOV = "https://api.adviserinfo.sec.gov/search";

	private static final String HTTPS_API_BROKERCHECK_FINRA_ORG = "https://api.brokercheck.finra.org/search";

	private static final String ERROR_STRING_LITERAL = "NULL";

	private static final String MASKED_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

	private static final String POLITE_USER_AGENT = "glycon-crawler @ Southampton University (ms2u19@soton.ac.uk)";

	public String getAdviserinfoJSON(String managerCRD) {

		String url = HTTPS_API_ADVISERINFO_SEC_GOV + "/individual/" + managerCRD
				+ "?hl=true&nrows=12&query=&start=0&wt=json";

		return sendAndGetResponse(url, MASKED_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());
	}

	public String getFirmJSON(String unfriendlyFirmName) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/firm?hl=true&json.wrf=angular.callbacks._0&nrows=12&query="
				+ unfriendlyFirmName + "&r=25&sort=score+desc&wt=json";

		return sendAndGetResponse(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());
	}

	public String getFirmManagersByExperienceJSON(int firmID, String experienceBoundry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG
				+ "/individual?filter=broker%3Dtrue,ia%3Dtrue,brokeria%3Dtrue,active%3Dtrue,prev%3Dtrue,bar%3Dtrue,experience%3D"
				+ experienceBoundry + "&firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._s&nrows=12&r=25&sort=score+desc&start=0&wt=json";

		return sendAndGetResponse(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());
	}

	public String getFirmManagersByRangeAndAlphabeticalJSON(String firmCRD, int entries, int startEntry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual?firm=" + firmCRD
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._0&nrows=" + entries
				+ "&r=25&sort=score+desc&start=" + startEntry + "&wt=json";

		return sendAndGetResponse(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());

	}

	public String getFirmManagersByRangeAndExpirienceJSON(String firmID, int entries, int startEntry,
			int startExperienceBoundry, String endExperienceBoundry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG
				+ "/individual?filter=broker%3Dtrue,ia%3Dtrue,brokeria%3Dtrue,active%3Dtrue,prev%3Dtrue,bar%3Dtrue,experience%3D"
				+ startExperienceBoundry + "-" + endExperienceBoundry + "&firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._s&nrows=" + entries
				+ "&r=25&sort=bc_lastname_sort+asc,bc_firstname_sort+asc,bc_middlename_sort+asc,score+desc&start="
				+ startEntry + "&wt=json";

		return sendAndGetResponse(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());

	}

	public String getFirmManagersByRangeJSON(String firmID, int entries, int startEntry) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual?firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._0&nrows=" + entries
				+ "&r=25&sort=bc_lastname_sort+asc,bc_firstname_sort+asc,bc_middlename_sort+asc,score+desc&start="
				+ startEntry + "&wt=json";

		return sendAndGetResponse(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());
	}

	public String getManagerFinraJSON(String managerCRD) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual/" + managerCRD
				+ "?json.wrf=angular.callbacks._0&wt=json";

		return sendAndGetResponse(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());
	}

	public String probeFirmHits(String firmID) {

		String url = HTTPS_API_BROKERCHECK_FINRA_ORG + "/individual?firm=" + firmID
				+ "&hl=true&includePrevious=true&json.wrf=angular.callbacks._0&nrows=0&r=25&sort=score+desc&wt=json";

		return sendAndGetResponse(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());
	}

	public byte[] getPDF(String firmID) {

		String url = "https://files.brokercheck.finra.org/individual/individual_" + firmID + ".pdf";

		return readFully(url, POLITE_USER_AGENT, RequestTimeEnum.DEV_TIME_PER_REQUEST.getValue());
	}

	private String sendAndGetResponse(String url, String userAgent, int requestTime) {

		try {

			HttpURLConnection con = prepareConnection(url, userAgent, requestTime);

			try (InputStream is = con.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

				int read = 0;
				byte[] buffer = new byte[4096];

				while ((read = is.read(buffer)) > 0) {
					baos.write(buffer, 0, read);
				}

				return new String(baos.toByteArray(), StandardCharsets.UTF_8);

			}

		} catch (IOException e) {

			return ERROR_STRING_LITERAL;

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();

			return ERROR_STRING_LITERAL;

		}

	}

	public static byte[] readFully(String url, String userAgent, int requestTime) {

		try {

			HttpURLConnection con = prepareConnection(url, userAgent, requestTime);

			try (InputStream is = con.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

				byte[] buffer = new byte[8192];

				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					baos.write(buffer, 0, bytesRead);
				}

				return baos.toByteArray();

			}

		} catch (IOException e) {

			return new byte[0];

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();

			return new byte[0];

		}

	}

	private static HttpURLConnection prepareConnection(String url, String userAgent, int requestTime)
			throws InterruptedException, IOException {

		URL urlObj;
		Thread.sleep(requestTime);

		urlObj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// Set timeout
		con.setConnectTimeout(RequestTimeEnum.CONNECTION_TIMEOUT.getValue());

		con.setReadTimeout(RequestTimeEnum.CONNECTION_TIMEOUT.getValue());

		// add request header
		con.setRequestProperty("User-Agent", userAgent);
		return con;
	}

}
