package glycon.object;

import glycon.network.RequestURL;
import glycon.parser.json.JSONParserManager;

public class FirmManagerIn extends FirmManager {

	public FirmManagerIn(String ind_source_id, String ind_firstname, String[] ind_other_names, String ind_lastname) {

		this.ind_source_id = ind_source_id;

		this.ind_firstname = ind_firstname;

		this.ind_lastname = ind_lastname;

		this.ind_other_names = ind_other_names;

	}

	public void generateInfo() {

		String managerFinraJSON = getJSON('F');

		this.firmFinraJSON = managerFinraJSON;

		String managerSecJSON = getJSON('C');

		this.firmSecJSON = managerSecJSON;

		JSONParserManager.parseManagerJSON(this);

	}

	private String getJSON(char c) {
		String managerJSON = "NULL";

		int failCount = 0;

		while (managerJSON.contentEquals("NULL") && failCount < 5) {

			if (c == 'F') {

				managerJSON = new RequestURL().getManagerFinraJSON(ind_source_id);

			} else {

				managerJSON = new RequestURL().getAdviserinfoJSON(ind_source_id);

			}

			failCount++;

		}
		return managerJSON;
	}

}
