/* Date:        April 4, 2011
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.3
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package org.molgenis.col7a1.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.html.Container;
import org.molgenis.framework.ui.html.HiddenInput;
import org.molgenis.framework.ui.html.IntInput;
import org.molgenis.framework.ui.html.SelectInput;
import org.molgenis.framework.ui.html.TextLineInput;
import org.molgenis.mutation.ServiceLocator;
import org.molgenis.mutation.dto.ExonDTO;
import org.molgenis.mutation.dto.ProteinDomainDTO;
import org.molgenis.mutation.service.SearchService;
import org.molgenis.mutation.ui.HtmlFormWrapper;
import org.molgenis.mutation.ui.search.SearchPlugin;
import org.molgenis.pheno.service.PhenoService;
import org.molgenis.util.tuple.Tuple;
import org.molgenis.util.ValueLabel;

public class Search extends SearchPlugin
{
	private static final long serialVersionUID = 1162846311691838788L;

	public Search(String name, ScreenController<?> parent)
	{
		super(name, parent);
		this.getModel().setPatientPager("res/mutation/col7a1/patientPager.jsp");
		this.getModel().setMutationPager("res/mutation/col7a1/mutationPager.jsp");
		this.getModel().setPatientViewer("/org/molgenis/col7a1/ui/patient.ftl");
		this.getModel().setMutationViewer("/org/molgenis/col7a1/ui/mutation.ftl");
		this.getModel().setExpertSearchFormWrapper(new HtmlFormWrapper(new ExpertSearchForm()));
	}

	@Override
	protected void handleShowPatient(Tuple request)
	{
		super.handleShowPatient(request);

		/*
		 * Add some phenotypic details that will be displayed in the patient view already:
		 * "IF", "EM" and "Patient material" information
		 */
		if (StringUtils.isNotEmpty(request.getString("pid")))
		{
			this.getModel().setIndividualDTO(this.phenoService.findPhenotypeDetails(this.getModel().getPatientSummaryVO().getPatientId()));
		}
	}

	@SuppressWarnings("unchecked")
	protected void populateExpertSearchForm()
	{
		PhenoService phenoService = ServiceLocator.instance().getPhenoService();
		SearchService searchService = ServiceLocator.instance().getSearchService();

		Container expertSearchForm = this.getModel().getExpertSearchFormWrapper().getForm();

		((HiddenInput) expertSearchForm.get("__target")).setValue(this.getName());
		((HiddenInput) expertSearchForm.get("select")).setValue(this.getName());

		if (this.getModel().getMutationSearchCriteriaVO().getVariation() != null) ((TextLineInput<String>) expertSearchForm
				.get("variation")).setValue(this.getModel().getMutationSearchCriteriaVO().getVariation());

		if (this.getModel().getMutationSearchCriteriaVO().getCdnaPosition() != null) ((IntInput) expertSearchForm
				.get("nuclno")).setValue(this.getModel().getMutationSearchCriteriaVO().getCdnaPosition());

		if (this.getModel().getMutationSearchCriteriaVO().getCodonNumber() != null) ((IntInput) expertSearchForm
				.get("aano")).setValue(this.getModel().getMutationSearchCriteriaVO().getCodonNumber());

		List<ValueLabel> exonIdOptions = new ArrayList<ValueLabel>();
		exonIdOptions.add(new ValueLabel("", "Select"));
		for (ExonDTO exonSummaryVO : searchService.findAllExons())
			exonIdOptions.add(new ValueLabel(exonSummaryVO.getId(), exonSummaryVO.getName()));
		((SelectInput) expertSearchForm.get("exon_id")).setOptions(exonIdOptions);
		if (this.getModel().getMutationSearchCriteriaVO().getExonId() != null) ((SelectInput) expertSearchForm
				.get("exon_id")).setValue(this.getModel().getMutationSearchCriteriaVO().getExonId());
		else
			((SelectInput) expertSearchForm.get("exon_id")).setValue("Select");

		List<ValueLabel> typeOptions = new ArrayList<ValueLabel>();
		typeOptions.add(0, new ValueLabel("", "Select"));
		for (String mutationType : searchService.getAllVariantTypes())
			typeOptions.add(new ValueLabel(mutationType, mutationType));
		((SelectInput) expertSearchForm.get("type")).setOptions(typeOptions);
		if (this.getModel().getMutationSearchCriteriaVO().getType() != null) ((SelectInput) expertSearchForm
				.get("type")).setValue(this.getModel().getMutationSearchCriteriaVO().getType());
		else
			((SelectInput) expertSearchForm.get("type")).setValue("Select");

		List<ValueLabel> consequenceOptions = new ArrayList<ValueLabel>();
		consequenceOptions.add(0, new ValueLabel("", "Select"));
		for (String consequence : phenoService.findObservedValues("consequence"))
			consequenceOptions.add(new ValueLabel(consequence, consequence));
		((SelectInput) expertSearchForm.get("consequence")).setOptions(consequenceOptions);
		if (this.getModel().getMutationSearchCriteriaVO().getConsequence() != null) ((SelectInput) expertSearchForm
				.get("consequence")).setValue(this.getModel().getMutationSearchCriteriaVO().getConsequence());
		else
			((SelectInput) expertSearchForm.get("consequence")).setValue("Select");

		List<ValueLabel> domainOptions = new ArrayList<ValueLabel>();
		domainOptions.add(new ValueLabel("", "Select"));
		for (ProteinDomainDTO domainVO : searchService.findAllProteinDomains())
			domainOptions.add(new ValueLabel(domainVO.getDomainId(), domainVO.getDomainName()));
		((SelectInput) expertSearchForm.get("domain_id")).setOptions(domainOptions);
		if (this.getModel().getMutationSearchCriteriaVO().getProteinDomainId() != null) ((SelectInput) expertSearchForm
				.get("domain_id")).setValue(this.getModel().getMutationSearchCriteriaVO().getProteinDomainId());
		else
			((SelectInput) expertSearchForm.get("domain_id")).setValue("Select");

		List<ValueLabel> phenotypeOptions = new ArrayList<ValueLabel>();
		phenotypeOptions.add(new ValueLabel("", "Select"));
		for (String phenotypeName : phenoService.findObservedValues("Phenotype"))
			phenotypeOptions.add(new ValueLabel(phenotypeName, phenotypeName));
		((SelectInput) expertSearchForm.get("phenotype")).setOptions(phenotypeOptions);
		if (this.getModel().getMutationSearchCriteriaVO().getPhenotypeId() != null) ((SelectInput) expertSearchForm
				.get("phenotype")).setValue(this.getModel().getMutationSearchCriteriaVO().getPhenotypeId());
		else
			((SelectInput) expertSearchForm.get("phenotype")).setValue("Select");

		List<ValueLabel> inheritanceOptions = new ArrayList<ValueLabel>();
		inheritanceOptions.add(0, new ValueLabel("", "Select"));
		for (String inheritance : phenoService.findObservedValues("Inheritance"))
			inheritanceOptions.add(new ValueLabel(inheritance, inheritance));
		((SelectInput) expertSearchForm.get("inheritance")).setOptions(inheritanceOptions);
		if (this.getModel().getMutationSearchCriteriaVO().getInheritance() != null) ((SelectInput) expertSearchForm
				.get("inheritance")).setValue(this.getModel().getMutationSearchCriteriaVO().getInheritance());
		else
			((SelectInput) expertSearchForm.get("inheritance")).setValue("Select");
	}
}
