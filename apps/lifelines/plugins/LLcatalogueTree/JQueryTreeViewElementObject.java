package plugins.LLcatalogueTree;

import org.molgenis.auth.Institute;
import org.molgenis.framework.ui.html.JQueryTreeViewElement;
import org.molgenis.pheno.Category;
import org.molgenis.pheno.Measurement;

public class JQueryTreeViewElementObject extends JQueryTreeViewElement{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Label of the tree (that can be made also linkable)  **/ 
	String label;
	Measurement measurement;
	Category category;
	Institute institute;

	private JQueryTreeViewMeasurement treeView;
	
	public JQueryTreeViewElementObject(Measurement measurement,Category category,Institute institute, JQueryTreeViewElementObject parent){
		super(measurement.getName(), parent);
		
		//this.setLabel("<a href=\"#\" onclick=\"$('#leftSide').attr('value','selectNode'); jQuery('#measureId').attr('value','"+measurement.getId()+"'); $('#plugins_LLcatalogueTree_LLcatalogueTreePlugin').submit();\" >"+ name +"</a>");
		this.setLabel(name);
		setMeasurementObject(measurement);
		setCategoryObject(category);
		setInstituteObject(institute);
	}

	
	public JQueryTreeViewElementObject(String name, JQueryTreeViewElementObject parent) {
		super(name, parent);
		measurement = null;
		category = null;
		institute = null;
	}
	
	public JQueryTreeViewElementObject(String name, JQueryTreeViewElementObject parent, String url)
	{
		super(name, parent, url);
		measurement = null;
		category = null;
		institute = null;
	}

	public Measurement getMeasurementObject(){
		return measurement;
	}
	
	public void setMeasurementObject(Measurement measurement){
		this.measurement = measurement;
	}

	public Category getCategoryObject(){
		return category;
	}
	
	private void setCategoryObject(Category category) {
		this.category = category;
	}

	public Institute getInstituteObject(){
		return institute;
	}
	
	private void setInstituteObject(Institute institute) {
		this.institute = institute;
	}
	
	public void setTreeView (JQueryTreeViewMeasurement<JQueryTreeViewElementObject> treeView) {
		this.treeView = treeView;
	}
	
	
	
}