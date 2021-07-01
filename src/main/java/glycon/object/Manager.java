package glycon.object;

import java.util.ArrayList;
import java.util.List;

import glycon.object.manager.CurrentEmployment;
import glycon.object.manager.Examination;
import glycon.object.manager.PreviousEmployment;

public abstract class Manager {

	protected List<CurrentEmployment> currentMangerEmployments = new ArrayList<>();

	private List<Disclosure> discolsures = new ArrayList<>();

	protected List<Examination> examinations = new ArrayList<>();

	protected String managerFinraJSON;

	protected String managerSecJSON;

	private String ind_bc_disclosure_fl;

	protected String ind_firstname;

	protected String ind_lastname;

	private String ind_middlename;

	protected String[] ind_other_names;

	protected String ind_source_id;

	protected List<PreviousEmployment> previousMangerEmployments = new ArrayList<>();

	public List<CurrentEmployment> getCurrentMangerEmployments() {
		return currentMangerEmployments;
	}

	public List<Disclosure> getDiscolsures() {
		return discolsures;
	}

	public List<Examination> getExaminations() {
		return examinations;
	}

	public String getManagerFinraJSON() {
		return managerFinraJSON;
	}

	public String getFirmSecJSON() {
		return managerSecJSON;
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
		return !this.currentMangerEmployments.isEmpty() ? this.currentMangerEmployments.get(0).getFirmId()
				: this.previousMangerEmployments.get(0).getFirmId();
	}

	public List<PreviousEmployment> getPreviousMangerEmployments() {
		return previousMangerEmployments;
	}

	public void setCurrentMangerEmployments(List<CurrentEmployment> currentMangerEmployments) {
		this.currentMangerEmployments = currentMangerEmployments;
	}

	public void setDiscolsures(List<Disclosure> discolsures) {
		this.discolsures = discolsures;
	}

	public void setExaminations(List<Examination> examinations) {
		this.examinations = examinations;
	}

	public void setManagerFinraJSON(String firmManagerJSON) {
		this.managerFinraJSON = firmManagerJSON;
	}

	public void setFirmSecJSON(String firmSecJSON) {
		this.managerSecJSON = firmSecJSON;
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

}
