package glycon.object;

import java.util.ArrayList;
import java.util.List;

import glycon.network.RequestURL;
import glycon.parser.JSONParser;

public abstract class FirmManager {

	protected String firmFinraJSON;

	protected String firmSecJSON;

	protected String ind_source_id;

	protected String ind_firstname;

	private String ind_middlename;

	protected String ind_lastname;

	private String ind_bc_disclosure_fl;

	protected String[] ind_other_names;

	private List<Disclosure> discolsuresFinra = new ArrayList<>();

	private List<Disclosure> discolsuresSEC = new ArrayList<>();

	protected List<CurrentEmployment> currentMangerEmployments = new ArrayList<>();

	protected List<CurrentEmployment> currentMangerIAEmployments = new ArrayList<>();

	protected List<PreviousEmployment> previousMangerEmployments = new ArrayList<>();

	protected List<PreviousEmployment> previousMangerIAEmployments = new ArrayList<>();

	public List<CurrentEmployment> getCurrentMangerEmployments() {
		return currentMangerEmployments;
	}

	public List<CurrentEmployment> getCurrentMangerIAEmployments() {
		return currentMangerIAEmployments;
	}

	public List<Disclosure> getDiscolsuresFinra() {
		return discolsuresFinra;
	}

	public String getFirmFinraJSON() {
		return firmFinraJSON;
	}

	public String getFirmSecJSON() {
		return firmSecJSON;
	}

	public String getInd_bc_disclosure_fl() {
		return ind_bc_disclosure_fl;
	}

	public String getInd_firstname() {
		return ind_firstname;
	}

	public String getInd_lastname() {
		return ind_lastname;
	}

	public String getInd_middlename() {
		return ind_middlename;
	}

	public String[] getInd_other_names() {
		return ind_other_names;
	}

	public String getInd_source_id() {
		return ind_source_id;
	}

	public String getMostRecentFirmId() {

		Employment finraMostRecent = !this.currentMangerEmployments.isEmpty() ? this.currentMangerEmployments.get(0)
				: this.previousMangerEmployments.get(0);

		Employment secMostRecentFirmId = !this.currentMangerIAEmployments.isEmpty()
				? this.currentMangerIAEmployments.get(0)
				: this.previousMangerIAEmployments.get(0);

		Employment mostRecentFirmId = finraMostRecent.compareTo(secMostRecentFirmId) > -1 ? finraMostRecent
				: secMostRecentFirmId;

		return mostRecentFirmId.getFirmId();
	}

	public List<PreviousEmployment> getPreviousMangerEmployments() {
		return previousMangerEmployments;
	}

	public List<PreviousEmployment> getPreviousMangerIAEmployments() {
		return previousMangerIAEmployments;
	}

	public void setCurrentMangerEmployments(List<CurrentEmployment> currentMangerEmployments) {
		this.currentMangerEmployments = currentMangerEmployments;
	}

	public void setCurrentMangerIAEmployments(List<CurrentEmployment> currentMangerIAEmployments) {
		this.currentMangerIAEmployments = currentMangerIAEmployments;
	}

	public void setDiscolsuresFinra(List<Disclosure> discolsures) {
		this.discolsuresFinra = discolsures;
	}

	public void setFirmFinraJSON(String firmFinraJSON) {
		this.firmFinraJSON = firmFinraJSON;
	}

	public void setFirmSecJSON(String firmSecJSON) {
		this.firmSecJSON = firmSecJSON;
	}

	public void setInd_bc_disclosure_fl(String ind_bc_disclosure_fl) {
		this.ind_bc_disclosure_fl = ind_bc_disclosure_fl;
	}

	public void setInd_firstname(String ind_firstname) {
		this.ind_firstname = ind_firstname;
	}

	public void setInd_lastname(String ind_lastname) {
		this.ind_lastname = ind_lastname;
	}

	public void setInd_middlename(String ind_middlename) {
		this.ind_middlename = ind_middlename;
	}

	public void setInd_other_names(String[] ind_other_names) {
		this.ind_other_names = ind_other_names;
	}

	public void setInd_source_id(String ind_source_id) {
		this.ind_source_id = ind_source_id;
	}

	public void setPreviousMangerEmployments(List<PreviousEmployment> previousMangerEmployments) {
		this.previousMangerEmployments = previousMangerEmployments;
	}

	public void setPreviousMangerIAEmployments(List<PreviousEmployment> previousMangerIAEmployments) {
		this.previousMangerIAEmployments = previousMangerIAEmployments;
	}

	public List<Disclosure> getDiscolsuresSEC() {
		return discolsuresSEC;
	}

	public void setDiscolsuresSEC(List<Disclosure> discolsuresSEC) {
		this.discolsuresSEC = discolsuresSEC;
	}

}
